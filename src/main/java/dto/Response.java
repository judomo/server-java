package dto;

import com.sun.net.httpserver.HttpExchange;
import lombok.Data;

@Data
public class Response {

    String text = "";
    Object data;
    Integer statusCode;
    HttpExchange httpExchange;

}

