package TCP;

import java.io.IOException;

public class RunServer {
    public static void main(String[] args) {

        try {
            new Server(9090);
        } catch (IOException e) {

        }
    }

}
