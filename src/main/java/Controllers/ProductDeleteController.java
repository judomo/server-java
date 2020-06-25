package Controllers;

import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class ProductDeleteController {

    public static void deleteProductById(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");

        URI requestUri = httpExchange.getRequestURI();

        String path = requestUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        int productId = Integer.parseInt(idStr);

        Response response = new Response();

        if ((!httpExchange.getRequestMethod().toLowerCase().equals("delete")) || productId < 0) {

            String data = "Conflict";

            response.setStatusCode(409);
            response.setData(data);

        }

        else {

            Integer res = daoProduct.delete(productId);

            if (res == null) {

                String data = "Product not found";

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

        daoProduct.close();

    }

}
