package service;

import dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtService {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public static String createJWT(Integer id, String login, String role) {

        //We will sign our JWT with our ApiKey secret

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Key signingKey = SECRET_KEY;

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id.toString()).setIssuedAt(now)
                .setSubject(login)
                .setIssuer(role)
                .signWith(signingKey);


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }


    public static String parseJWT(String jwt) throws ParseException {

        String jwtToken = jwt;

        String[] split_string = jwtToken.split("\\.");

        String base64EncodedBody = split_string[1];


        Base64 base64Url = new Base64(true);

        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");

        String body = new String(base64Url.decode(base64EncodedBody));
        System.out.println(body);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(body);

        System.out.println(json.get("sub"));

        return json.get("sub").toString();

    }

}
