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
import java.io.OutputStream;
import java.util.Map;

public class GroupPutController{

    public static void create(HttpExchange httpExchange) throws IOException {

        DaoGroup daoGroup = new DaoGroup();

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String postRequest = bufferedReader.readLine();

            Map<String, Object> postRequestParameters = HttpUtil.parseQuery(postRequest);

            JsonObject product_group = new JsonObject();

            for (Map.Entry<String, Object> entry : postRequestParameters.entrySet()) {

                System.out.println(entry.getKey() + ":" + entry.getValue().toString());

                product_group.addProperty(entry.getKey(), entry.getValue().toString());

            }

            Response response = new Response();

            if ((postRequestParameters.get("productGroupName").toString().length()) <= 0 || postRequestParameters.get("productGroupDescr").toString().length() == 0 ||  !httpExchange.getRequestMethod().toLowerCase().equals("put")) {

                String data = "Conflict";

                response.setData(data);
                response.setStatusCode(409);

            } else {



                Integer result = daoGroup.insertProductGroup(postRequestParameters.get("productGroupName").toString(),postRequestParameters.get("productGroupDescr").toString());



                if (result == -1) {

                    String data = "Group name exists";

                    response.setData(data);
                    response.setStatusCode(409);

                } else {

                    product_group.addProperty("id", result);

                    response.setData(product_group);
                    response.setStatusCode(201);

                }
            }

            response.setText("group-insert");

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

            daoGroup.close();

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }


}
