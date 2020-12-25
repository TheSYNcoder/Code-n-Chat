package TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 9090));

            ExecutorService service = Executors.newFixedThreadPool(2);
            service.execute(() -> listen(client));
            service.execute(() -> send(client));


        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    private static void send(SocketChannel socketChannel) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter data > ");
            while (true) {

                String in = sc.nextLine();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(in.getBytes());
                buffer.flip();
                int bytesWritten = socketChannel.write(buffer);
//                System.out.println(String.format("Sending Message: %s\nbufforBytes: %d", in, bytesWritten));

            }
        } catch (IOException e) {

        }
    }

    private static void listen(SocketChannel socketChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true) {
                StringBuilder stringBuilder = new StringBuilder();
                buffer.clear();
                int read = 0;

                read = socketChannel.read(buffer);
                // for reading
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                stringBuilder.append(new String(bytes));

                if (stringBuilder.toString().length() > 0) {
                    System.out.print(stringBuilder.toString());
//                    System.out.println();

                }
            }

        } catch (IOException e) {

        }
    }
}
