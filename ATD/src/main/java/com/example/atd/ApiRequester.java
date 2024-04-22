package com.example.atd;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class ApiRequester {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String sendRequest(String url, String method, Map<String, String> headers, String body) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));

        // Ajoutez les en-têtes à la requête
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String sendJsonRequest(String url, String method, Map<String, String> headers, Object body) throws Exception {
        // Utilisez Gson pour sérialiser l'objet en JSON
        Gson gson = new Gson();
        String jsonBody = gson.toJson(body);

        // Ajoutez l'en-tête "Content-Type" pour indiquer que le corps est au format JSON
        Map<String, String> headersWithContentType = headers != null ? headers : new HashMap<>();
        headersWithContentType.put("Content-Type", "application/json");

        return sendRequest(url, method, headersWithContentType, jsonBody);
    }
}
