package Controllers;

import DAO.DaoGroup;
import DAO.DaoProduct;
import com.sun.net.httpserver.HttpExchange;
import dto.Response;

import java.io.IOException;
import java.net.URI;

public class GroupDeleteController{


    public static void deleteGroupById(HttpExchange httpExchange) throws IOException {

        DaoGroup daoGroup = new DaoGroup();

        URI requestUri = httpExchange.getRequestURI();

        String path = requestUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        int groupId = Integer.parseInt(idStr);

        Response response = new Response();

        if ((!httpExchange.getRequestMethod().toLowerCase().equals("delete")) || groupId <= 0) {

            String data = "Conflict";

            response.setStatusCode(409);
            response.setData(data);

        } else {

            Integer res = daoGroup.delete(groupId);

            if (res == null) {

                String data = "Group not found";

                response.setStatusCode(404);

                response.setData(data);

            } else {

                response.setStatusCode(204);

            }

        }

        response.setText("good-get");

        response.setHttpExchange(httpExchange);

        httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:3000");
        httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, Access-Control-Allow-Credentials, Access-Control-Allow-Origin, Access-Control-Expose-Headers, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Methods, Authorization");


        httpExchange.sendResponseHeaders(response.getStatusCode(), -1);

        daoGroup.close();

    }
}

