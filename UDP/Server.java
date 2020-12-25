package UDP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server {


    final private int PORT = 9090;
    DatagramChannel datagramChannel;


    public Server() throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress("localhost", 9090));
        System.out.println("Server started on port 9090");
        System.out.println("Listening ..... ");
        startServerLoop();
    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private void startServerLoop() throws IOException {
        while (true) {
            // blocking
            datagramChannel.receive(buffer);
            buffer.flip();
            StringBuilder sb = new StringBuilder();
            while (buffer.hasRemaining()) {
                sb.append((char) buffer.get());
            }
            System.out.println(sb.toString());
            // clear the buffer for next message
            buffer.clear();
        }
    }


    public static void main(String[] args) {
        // start looping
        try {
            new Server();
        } catch (IOException e) {
            System.out.println("[ERR] IOExecption" + e.getMessage());
            e.printStackTrace();
        }
    }


}
