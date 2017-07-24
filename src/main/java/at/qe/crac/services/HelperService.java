package at.qe.crac.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@Scope("application")
public class HelperService {

    private String token;

    public JsonNode request(String path, String method) {

        List<SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new SimpleEntry<>("Authorization", "Basic Og=="));
        properties.add(new SimpleEntry<>("Token", token));

        HttpURLConnection con = connection(path, method, properties);
        if(con == null) {
            return null;
        }
        return response(con);
    }

    public JsonNode request(String path, String method, JsonNode payload) {

        List<SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new SimpleEntry<>("Authorization", "Basic Og=="));
        properties.add(new SimpleEntry<>("Token", token));

        properties.add(new SimpleEntry<>("Content-Type", "application/json"));
        properties.add(new SimpleEntry<>("Accept", "application/json"));
        //properties.add(new SimpleEntry<>("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"));

        HttpURLConnection con = connection(path, method, properties);
        if(con == null) {
            System.out.print("connection");
            return null;
        }

        con.setDoOutput(true);
        OutputStreamWriter out;
        try {
            out = new OutputStreamWriter(con.getOutputStream());
            out.write(payload.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response(con);
    }

    private HttpURLConnection connection(String path, String method) {
        try {
            String baseURL = "https://core.crac.at/crac-core";
            String url = baseURL + path;

            URL obj = null;
            obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(method.toUpperCase());

            return con;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection connection(String path, String method, List<SimpleEntry<String, String>> properties) {
        HttpURLConnection con = connection(path, method);
        if(con == null) {
            return null;
        }
        for(SimpleEntry<String, String> property : properties) {
            con.setRequestProperty(property.getKey(), property.getValue());
        }

        return con;
    }

    private JsonNode response(HttpURLConnection con) {
        try {
            switch(con.getResponseCode()) {
                case 401:
                    System.out.println("401");
                    return null;
                case 404:
                    System.out.println("404");
                    return null;
                case 400:
                    System.out.println(con.getResponseMessage());
                    System.out.println(con);
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder message = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                message.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(message.toString());

            if(response == null || !response.path("success").asBoolean()) {
                System.out.print("response");
                return null;
            }

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode login(String username, String password) {

        String auth =  username + ':' + password;
        final byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
        final String encoded = Base64.getEncoder().encodeToString(authBytes);

        List<SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new SimpleEntry<String, String>("Authorization", "Basic "+encoded));

        HttpURLConnection con = connection("/user/login", "get", properties);
        if(con == null) {
            return null;
        }

        JsonNode response = response(con);
        if(response == null) {
            return null;
        }

        JsonNode object = response.path("object");
        token = object.path("code").asText();

        return response;
    }

    public JsonNode logout() {
        if(token == null) {
            return null;
        }

        JsonNode response = request("/user/logout", "get");
        token = null;

        return response;
    }
}
