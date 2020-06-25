package Controllers;

import DAO.DaoProduct;
import DAO.UserDao;
import com.sun.net.httpserver.HttpExchange;
import dto.Response;
import dto.User;
import org.apache.commons.codec.digest.DigestUtils;
import service.JwtService;
import utils.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class UserAuthController {

    private static UserDao USER_DAO = new UserDao("storedb");

    public static void login(HttpExchange httpExchange) throws IOException {


        URI requestUri = httpExchange.getRequestURI();

        Map<String, Object> searchParams = HttpUtil.parseQuery(requestUri.getRawQuery());

        String userLogin = String
                .valueOf((String) searchParams.get("login"));

        String userPass = String
                .valueOf((String) searchParams.get("password"));

        final User user = USER_DAO.getByLogin(userLogin);

        Response response = new Response();

        httpExchange.getResponseHeaders()
                .add("Content-Type", "application/json");


        if (user != null) {

            if (user.getPassword().equals(DigestUtils.md5Hex(userPass))) {

                String data = JwtService.createJWT(user.getId(), user.getLogin(), user.getRole());

                response.setStatusCode(200);
                response.setData(data);

            } else {

                String data = "invalid password";

                response.setStatusCode(401);
                response.setData(data);

            }

        } else {

            response.setStatusCode(401);
            response.setData("unknown user");

        }


        response.setText("good-delete");

        response.setHttpExchange(httpExchange);

        httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");

        httpExchange.sendResponseHeaders(response.getStatusCode(), response.getData().toString().length());

        OutputStream os = httpExchange.getResponseBody();

        os.write(response.getData().toString().getBytes());

        os.close();

    }

}
