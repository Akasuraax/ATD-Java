package com.example.atd;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.example.atd.exception.ApiRequestException;
import com.google.gson.Gson;

public class ApiRequester {

    private static final String BASE_URL = "http://127.0.0.1:8000/api/";

    public static HttpResponse<String> postRequest(String url, Map<String, String> requestBody) throws ApiRequestException {
        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Convertir le Map en JSON
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);

        // Créer une requête POST avec le corps JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (java.net.ConnectException e) {
            throw new ApiRequestException("Erreur de connexion : " + e.getMessage());
        } catch (Exception e) {
            throw new ApiRequestException("Erreur inattendue : " + e.getMessage());
        }
    }

    public static HttpResponse<String> patchRequest(String url, Map<String, String> requestBody) throws ApiRequestException {
        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Convertir le Map en JSON
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);

        // Créer une requête PATCH avec le corps JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + url))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getUserToken()) // Ajouter le token d'authentification
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (java.net.ConnectException e) {
            throw new ApiRequestException("Erreur de connexion : " + e.getMessage());
        } catch (Exception e) {
            throw new ApiRequestException("Erreur inattendue : " + e.getMessage());
        }
    }
    public static HttpResponse<String> getRequest(String url) throws ApiRequestException {
        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Créer une requête GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + url))
                .header("Authorization", SessionManager.getInstance().getUserToken()) // Ajouter le token d'authentification
                .GET()
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (java.net.ConnectException e) {
            throw new ApiRequestException("Erreur de connexion : " + e.getMessage());
        } catch (Exception e) {
            throw new ApiRequestException("Erreur inattendue : " + e.getMessage());
        }
    }
}
