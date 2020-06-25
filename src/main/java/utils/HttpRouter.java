package utils;

import Controllers.*;
import DAO.UserDao;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;

import dto.Group;
import dto.Product;
import dto.User;
import service.JwtService;

public class HttpRouter {


    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final HttpPrincipal ANONYMOUS_USER = new HttpPrincipal("anonymous", "anonymous");

    public static final UserDao USER_DAO = new UserDao("storedb");

    public static void setup(HttpServer server) {

        server
                .createContext("/api/login", UserAuthController::login);

        server
                .createContext("/api/good", ProductPutController::create).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/getOne", ProductGetController::getProductById).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/getStats", ProductGetStat::getAllStats).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/getAll", ProductGetAllController::getAllProducts).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/search", ProductsSearchController::getAllProducts).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/update", ProductPostController::update).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/good/delete/", ProductDeleteController::deleteProductById).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/group", GroupPutController::create).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/group/update", GroupPostController::update).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/group/delete", GroupDeleteController::deleteGroupById).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/group/getOne", GroupGetController::getProductsGroupById).setAuthenticator(new MyAuthenticator());

        server
                .createContext("/api/group/getAll", GroupGetAllController::getAllGroups).setAuthenticator(new MyAuthenticator());




        System.out.println(
                new StringBuilder()
                        .append("Routes to the HttpServer were configured successfully!")
                        .toString()
        );
    }

    private static class MyAuthenticator extends Authenticator {

        @Override
        public Result authenticate(final HttpExchange httpExchange) {

            final String token = httpExchange.getRequestHeaders().getFirst(AUTHORIZATION_HEADER);

            System.out.println(token);

            if (token != null) {
                try {

                    String username = JwtService.parseJWT(token);

                    User user = HttpRouter.USER_DAO.getByLogin(username);

                    if (user != null) {
                        return new Success(new HttpPrincipal(username, user.getRole()));
                    } else {
                        return new Retry(401);
                    }

                } catch (Exception e) {

                    System.out.println(e.toString());

                    return new Failure(403);
                }
            }

            return new Success(ANONYMOUS_USER);
        }
    }
}
