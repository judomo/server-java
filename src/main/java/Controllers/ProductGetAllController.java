package Controllers;

import DAO.DaoProduct;

import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ProductGetAllController{


    public static void getAllProducts(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");

        JSONObject products_json = new JSONObject();



        Response response = new Response();


            ArrayList<Product> products = daoProduct.getAllProducts();

            if (products == null) {

                String data = "Product not found";

                response.setStatusCode(404);

                response.setData(data);

            } else {

                response.setStatusCode(200);


                JSONArray products_arr = new JSONArray();


                for (int i = 0; i < products.size(); i++) {

                    JSONObject tmp = new JSONObject();

                    tmp.put("product_id", products.get(i).getId());
                    tmp.put("product_name", products.get(i).getName());
                    tmp.put("product_descr", products.get(i).getDescr());
                    tmp.put("product_manuf", products.get(i).getManufacturer());
                    tmp.put("product_price", products.get(i).getPrice());
                    tmp.put("product_amount", products.get(i).getAmount());

                    tmp.put("product_group_id", products.get(i).getGroup_id());

                    tmp.put("product_group_name", products.get(i).getGroup_name());

                    products_arr.add(tmp);

                }

                products_json.put("products", products_arr);


                response.setData(products_json);

                System.out.println(products_json.toString());

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
