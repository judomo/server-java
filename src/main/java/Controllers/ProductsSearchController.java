package Controllers;

import DAO.DaoProduct;
import com.sun.net.httpserver.HttpExchange;
import dto.Product;
import dto.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.HttpUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

public class ProductsSearchController{


    public static void getAllProducts(HttpExchange httpExchange) throws IOException {

        DaoProduct daoProduct = new DaoProduct("storedb");

        URI requestUri = httpExchange.getRequestURI();

        Map<String, Object> searchParams = HttpUtil.parseQuery(requestUri.getRawQuery());

        String queryString = String
                .valueOf((String) searchParams.get("query"));


        JSONObject products_json = new JSONObject();

        Response response = new Response();

        System.out.println("Started");


        ArrayList<Product> products = daoProduct.searchProducts(queryString);

        System.out.println(products);

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
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Set-Cookie");



        httpExchange.sendResponseHeaders(response.getStatusCode(), response.getData().toString().length());

        OutputStream os = httpExchange.getResponseBody();

        os.write(response.getData().toString().getBytes());

        os.close();

        daoProduct.close();

    }


}
