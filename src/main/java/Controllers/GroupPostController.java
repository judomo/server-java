package Controllers;

import DAO.DaoGroup;
import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Response;
import utils.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class GroupPostController{

    public static void update(HttpExchange httpExchange) throws IOException {

        DaoGroup daoGroup = new DaoGroup();

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String postRequest = bufferedReader.readLine();

            Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);

            System.out.println(httpExchange.getRequestMethod().toLowerCase().equals("post"));

            JsonObject product = new JsonObject();

            for (Map.Entry<String, Object> entry : postRequestParameters.entrySet()) {

                product.addProperty(entry.getKey(), entry.getValue().toString());

            }

            Response response = new Response();

            if ((postRequestParameters.get("productGroupName").toString().length()) <= 0 || postRequestParameters.get("productGroupDesc").toString().length() == 0 ||  !httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                String data = "Conflict";

                response.setData(data);
                response.setStatusCode(409);

            }

            else {

                Integer result = daoGroup.update(Integer.parseInt((postRequestParameters.get("productGroupId").toString())), postRequestParameters.get("productGroupName").toString(), postRequestParameters.get("productGroupDesc").toString());

                if (result == -1) {

                    String data = "Product group name exists";

                    response.setData(data);
                    response.setStatusCode(409);

                } else if (result == null) {


                    String data = "Product group not found";

                    response.setData(data);
                    response.setStatusCode(404);

                } else {

                    response.setStatusCode(204);

                }
            }

            response.setText("product-group-update");

            response.setHttpExchange(httpExchange);

            httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:3000");
            httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, Access-Control-Allow-Credentials, Access-Control-Allow-Origin, Access-Control-Expose-Headers, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Methods, Authorization");


            httpExchange.sendResponseHeaders(response.getStatusCode(), -1);

            daoGroup.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
