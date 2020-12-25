package TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private ServerSocketChannel serverSocket;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private ExecutorService executor;


    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.socket().bind(new InetSocketAddress("localhost", port));
        this.serverSocket.configureBlocking(false);
        this.selector = Selector.open();


        this.serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> listen());
    }


    private void listen() {
        try {
            System.out.println("[INFO] Server starting on port " + this.port + " ....");
            Iterator<SelectionKey> iterator;
            SelectionKey key;
            while (this.serverSocket.isOpen()) {
                selector.select();
                iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("IOException, server of port " + this.port + " terminating. Stack trace:");
            e.printStackTrace();
        }
    }


    private final ByteBuffer welcomeMessage = ByteBuffer.wrap("Welcome to the server".getBytes());

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        String address = (new StringBuilder(socketChannel.socket().getInetAddress().toString())).append(":").append(socketChannel.socket().getPort()).toString();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, address);
        socketChannel.write(welcomeMessage);
        welcomeMessage.rewind();
        System.out.println("[INFO] Accepted connection from :" + address);
    }


    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((SocketChannel) key.channel());
        StringBuilder stringBuilder = new StringBuilder();

        buffer.clear();
        int read = 0;
        try {
            while ((read = socketChannel.read(buffer)) > 0) {
                // for reading
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                stringBuilder.append(new String(bytes));
                buffer.clear();
            }
        } catch (Exception e) {
            key.cancel();
            read = -1;
        }

        String message;

        if (read < 0) {
            message = key.attachment() + " left the server .";
            socketChannel.close();
        } else {
            message = key.attachment() + "  :  " + stringBuilder.toString() + "\nEnter data > ";
        }

        System.out.println("[INFO] SERVER GOT MESSAGE : " + stringBuilder.toString() + " from :" + key.attachment());
        broadcast_to_clients(message);

    }

    private void broadcast_to_clients(String message) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                socketChannel.write(buf);
                buf.rewind();
            }
        }
    }
}
