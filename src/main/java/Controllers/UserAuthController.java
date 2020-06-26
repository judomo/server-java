package Controllers;

import DAO.DaoProduct;
import DAO.UserDao;
import com.sun.net.httpserver.HttpExchange;
import dto.Response;
import dto.User;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import service.JwtService;
import utils.HttpUtil;
import utils.MyCipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class UserAuthController {

    private static UserDao USER_DAO = new UserDao("storedb");

    @SneakyThrows
    public static void login(HttpExchange httpExchange) throws IOException {


        URI requestUri = httpExchange.getRequestURI();

        Map<String, Object> searchParams = HttpUtil.parseQuery(requestUri.getRawQuery());

        String userLogin = String
                .valueOf((String) searchParams.get("login"));

        System.out.println(MyCipher.desEncrypt(userLogin));

        String userPass = String
                .valueOf((String) searchParams.get("password"));

        final User user = USER_DAO.getByLogin(MyCipher.desEncrypt(userLogin));

        Response response = new Response();

        httpExchange.getResponseHeaders()
                .add("Content-Type", "application/json");


        if (user != null) {

            if (user.getPassword().equals(DigestUtils.md5Hex(MyCipher.desEncrypt(userPass)))) {

                String data = JwtService.createJWT(user.getId(), user.getLogin(), user.getRole());

                response.setStatusCode(200);
                response.setData( MyCipher.encrypt(data));

            } else {

                String data = "invalid password";

                response.setStatusCode(401);
                response.setData(  MyCipher.encrypt(data));

            }

        } else {

            response.setStatusCode(401);
            response.setData(MyCipher.encrypt("unknown user"));

        }


        response.setText("good-delete");

        response.setHttpExchange(httpExchange);

        httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:3000");
        httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, Access-Control-Allow-Credentials, Access-Control-Allow-Origin, Access-Control-Expose-Headers, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Methods, Authorization");

        httpExchange.sendResponseHeaders(response.getStatusCode(), response.getData().toString().length());

        OutputStream os = httpExchange.getResponseBody();

        os.write(response.getData().toString().getBytes());

        os.close();

    }

}
