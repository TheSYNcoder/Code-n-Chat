package UDP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    DatagramChannel datagramChannel;
    private final int PORT = 9090;

    public static ArrayList<String> myList = new ArrayList<String>(
            Arrays.asList("Sending data",
                    "in ",
                    "through UDP",
                    "Socket"));

    public Client() throws IOException {
        datagramChannel = DatagramChannel.open();

        // the UDP method does not require
        datagramChannel.bind(null);
        execute();
    }

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    private void execute() throws IOException {

        for (String data : myList) {
            buffer.put(data.getBytes());
            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress("localhost", PORT));
            buffer.clear();
        }
    }


    public static void main(String[] args) {
        try {
            new Client();
        } catch (IOException e) {
            System.out.println("[ERR] IOExecption" + e.getMessage());
            e.printStackTrace();
        }
    }
}
