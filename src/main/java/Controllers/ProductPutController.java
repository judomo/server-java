package Controllers;

import DAO.DaoProduct;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;
import utils.HttpUtil;

import java.awt.geom.Arc2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import dto.Response;

public class ProductPutController {


    public static void create(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");


        try {

            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String postRequest = bufferedReader.readLine();

            Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);

            JsonObject product = new JsonObject();

            for (Map.Entry<String, Object> entry : postRequestParameters.entrySet()) {
//                System.out.println(entry.getKey() + ":" + entry.getValue().toString());

                product.addProperty(entry.getKey(), entry.getValue().toString());

            }


            Response response = new Response();

            if (Double.parseDouble(postRequestParameters.get("productPrice").toString()) <= 0 || postRequestParameters.get("productName").toString().length() == 0 || postRequestParameters.get("productDescr").toString().length() == 0 ||postRequestParameters.get("productDescr").toString().length() == 0 ||postRequestParameters.get("productManufacturer").toString().length() == 0 || Integer.parseInt(postRequestParameters.get("productAmount").toString()) <= 0 || Integer.parseInt(postRequestParameters.get("productGroupId").toString()) <= 0 || !httpExchange.getRequestMethod().toLowerCase().equals("put")) {

                String data = "Conflict";

                response.setData(data);
                response.setStatusCode(409);

            } else {

                Integer result = daoProduct.insertProduct(postRequestParameters.get("productName").toString(), Double.parseDouble(postRequestParameters.get("productPrice").toString()), postRequestParameters.get("productDescr").toString(), postRequestParameters.get("productManufacturer").toString(), Integer.parseInt(postRequestParameters.get("productAmount").toString()), Integer.parseInt(postRequestParameters.get("productGroupId").toString()));

                if (result == -1) {

                    String data = "Product name exists";

                    response.setData(data);
                    response.setStatusCode(409);

                } else {
                    product.addProperty("id", result);

                    response.setData(product);
                    response.setStatusCode(201);
                }
            }

            response.setText("good-insert");

            response.setHttpExchange(httpExchange);

            httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");

            httpExchange.sendResponseHeaders(response.getStatusCode(), response.getData().toString().length());

            OutputStream os = httpExchange.getResponseBody();

            os.write(response.getData().toString().getBytes());
            os.close();

            daoProduct.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
