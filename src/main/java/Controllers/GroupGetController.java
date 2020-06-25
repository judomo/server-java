package Controllers;

import DAO.DaoGroup;
import com.sun.net.httpserver.HttpExchange;
import dto.ProductsGroup;
import dto.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class GroupGetController{

    public static void getProductsGroupById(HttpExchange httpExchange) throws IOException {

        DaoGroup daoGroup = new DaoGroup();

        URI requestUri = httpExchange.getRequestURI();

        String path = requestUri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);

        JSONObject product_json = new JSONObject();

        int productId = Integer.parseInt(idStr);

        Response response = new Response();

        if ((!httpExchange.getRequestMethod().toLowerCase().equals("get")) || productId < 0) {

            String data = "Conflict";

            response.setStatusCode(409);
            response.setData(data);

        } else {

            ProductsGroup productsGroup = daoGroup.getById(productId);

            if (productsGroup == null) {

                String data = "Product not found";

                response.setStatusCode(404);

                response.setData(data);

            } else {

                response.setStatusCode(200);

                product_json.put("id", productsGroup.getId());

                product_json.put("group_name", productsGroup.getGroup_name());

                product_json.put("group_descr", productsGroup.getGroup_description());

                JSONArray list = new JSONArray();

                for (int i = 0; i < productsGroup.getProducts().size(); i++) {

                    JSONObject tmp = new JSONObject();

                    tmp.put("product_id", productsGroup.getProducts().get(i).getId());
                    tmp.put("product_name", productsGroup.getProducts().get(i).getName());
                    tmp.put("product_descr", productsGroup.getProducts().get(i).getDescr());
                    tmp.put("product_manuf", productsGroup.getProducts().get(i).getManufacturer());
                    tmp.put("product_price", productsGroup.getProducts().get(i).getPrice());
                    tmp.put("product_amount", productsGroup.getProducts().get(i).getAmount());

                    list.add(tmp);

                }

                product_json.put("products", list);

                response.setData(product_json);

                System.out.println(product_json.toString());


            }

            daoGroup.close();

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

    }
}




