package Controllers;

import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Response;
import utils.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class ProductPostController {


    public static void update(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String postRequest = bufferedReader.readLine();

            Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);

            System.out.println(httpExchange.getRequestMethod().toLowerCase().equals("post"));

            JsonObject product = new JsonObject();

            for (Map.Entry<String, Object> entry : postRequestParameters.entrySet()) {

                System.out.println(entry.getKey() + ":" + entry.getValue().toString());

                product.addProperty(entry.getKey(), entry.getValue().toString());

            }

            Response response = new Response();

            if (Integer.parseInt(postRequestParameters.get("productId").toString()) <= 0 || Double.parseDouble(postRequestParameters.get("productPrice").toString()) <= 0 || postRequestParameters.get("productName").toString().length() == 0 || postRequestParameters.get("productDescr").toString().length() == 0 ||postRequestParameters.get("productManufacturer").toString().length() == 0  || Integer.parseInt(postRequestParameters.get("productGroupId").toString()) <= 0 || !httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                String data = "Conflict";

                response.setData(data);
                response.setStatusCode(409);

            }

            else if (Integer.parseInt(postRequestParameters.get("productAmount").toString()) <= 0){

                Integer res = daoProduct.delete(Integer.parseInt(postRequestParameters.get("productId").toString()));

                if (res == null) {

                    String data = "Product not found";

                    response.setStatusCode(404);

                    response.setData(data);

                } else {

                    String data = "Product not found";

                    response.setData(data);
                    response.setStatusCode(201);

                }

            }

            else {

                Integer result = daoProduct.update(Integer.parseInt(postRequestParameters.get("productId").toString()), postRequestParameters.get("productName").toString(), Double.parseDouble(postRequestParameters.get("productPrice").toString()), postRequestParameters.get("productDescr").toString(), postRequestParameters.get("productManufacturer").toString() , Integer.parseInt(postRequestParameters.get("productAmount").toString()), Integer.parseInt(postRequestParameters.get("productGroupId").toString()));

                if (result == -1) {

                    String data = "Product name exists";

                    response.setData(data);
                    response.setStatusCode(409);

                } else if (result == null) {


                    String data = "Product not found";

                    response.setData(data);
                    response.setStatusCode(404);

                } else {

                    response.setData("All done");

                    response.setStatusCode(201);

                }
            }

            response.setText("good-insert");

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


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
