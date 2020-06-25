package Controllers;

import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;
import utils.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ProductGetController {

    public static void getProductById(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");


        URI requestUri = httpExchange.getRequestURI();

        String path = requestUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        JsonObject product_json = new JsonObject();

        int productId = Integer.parseInt(idStr);

        Response response = new Response();

        if ((!httpExchange.getRequestMethod().toLowerCase().equals("get")) || productId < 0) {

            String data = "Conflict";

            response.setStatusCode(409);
            response.setData(data);

        } else {

            Product product = daoProduct.getById(productId);

            if (product == null) {

                String data = "Product not found";

                response.setStatusCode(404);

                response.setData(data);

            } else {

                response.setStatusCode(200);

                product_json.addProperty("id", product.getId());

                product_json.addProperty("product_name", product.getName());

                product_json.addProperty("product_price", product.getPrice());

                product_json.addProperty("product_descr", product.getDescr());

                product_json.addProperty("product_manufacturer", product.getManufacturer());

                product_json.addProperty("product_amount", product.getAmount());

                product_json.addProperty("product_group_id", product.getGroup_id());

                product_json.addProperty("product_group_name", product.getGroup_name());



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