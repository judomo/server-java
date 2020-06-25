import com.sun.net.httpserver.HttpServer;

import utils.HttpRouter;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerRunner {



    public static void main(String[] args) {

        try {


            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HttpServerConfig.SERVER_PORT), 0);

            HttpRouter.setup(server);

            server.start();


        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}
