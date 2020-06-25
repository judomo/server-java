package Controllers;

import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

public class ProductGetStat{



    public static void getAllStats(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");

        JsonObject product_json = new JsonObject();

        Response response = new Response();

        if ((!httpExchange.getRequestMethod().toLowerCase().equals("get"))) {

            String data = "Conflict";

            response.setStatusCode(409);
            response.setData(data);

        } else {

            ArrayList<Integer> stats = daoProduct.getStat();

            if (stats == null) {

                String data = "Products not found";

                response.setStatusCode(404);

                response.setData(data);

            } else {

                response.setStatusCode(200);

                product_json.addProperty("group_amount", stats.get(0));

                product_json.addProperty("product_sum_price", stats.get(1));

                product_json.addProperty("product_amount", stats.get(2));

                response.setData(product_json);

                System.out.println(product_json.toString());

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


        httpExchange.sendResponseHeaders(response.getStatusCode(), response.getData().toString().length());

        OutputStream os = httpExchange.getResponseBody();

        os.write(response.getData().toString().getBytes());

        os.close();

        daoProduct.close();

    }


}
