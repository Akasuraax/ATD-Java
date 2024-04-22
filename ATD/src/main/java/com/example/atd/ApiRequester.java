package com.example.atd;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.google.gson.Gson;

public class ApiRequester {

    public static String postRequest(String url, Map<String, String> requestBody) {
        try {
            // Créer un client HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Convertir le Map en JSON
            Gson gson = new Gson();
            String json = gson.toJson(requestBody);

            // Créer une requête POST avec le corps JSON
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            // Renvoyer une chaîne vide ou un message d'erreur en cas d'exception
            return "";
        }
    }
}
