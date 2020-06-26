package Controllers;

import DAO.DaoGroup;
import DAO.DaoProduct;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dto.Group;
import dto.Product;
import dto.Response;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.MyCipher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class GroupGetAllController{

    @SneakyThrows
    public static void getAllGroups(HttpExchange httpExchange) throws IOException {

        DaoGroup daoGroup = new DaoGroup();

        JSONObject products_json = new JSONObject();

        Response response = new Response();


        ArrayList<Group> groups = daoGroup.getAllPGroups();

        if (groups == null) {

            String data = "Product not found";

            response.setStatusCode(404);

            response.setData(MyCipher.encrypt(data));

        } else {

            response.setStatusCode(200);


            JSONArray products_arr = new JSONArray();


            for (int i = 0; i < groups.size(); i++) {

                JSONObject tmp = new JSONObject();

                tmp.put("group_id", groups.get(i).getId());
                tmp.put("group_name", groups.get(i).getGroup_name());
                tmp.put("group_descr", groups.get(i).getGroup_description());

                products_arr.add(tmp);

            }

            products_json.put("groups", products_arr);


            response.setData(MyCipher.encrypt(products_json.toString()));

            System.out.println(products_json);

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

        daoGroup.close();

    }


}
