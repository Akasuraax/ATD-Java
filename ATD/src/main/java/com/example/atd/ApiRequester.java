package com.example.atd;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

import com.example.atd.exception.ApiRequestException;
import com.google.gson.Gson;

import javax.net.ssl.SSLContext;

public class ApiRequester {

    private static String baseUrl;

    static {
        try {
            Properties props = new Properties();
            String configFilePath = ApiRequester.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            configFilePath = configFilePath.substring(0, configFilePath.lastIndexOf('/')) + "/config.conf";
            FileInputStream fis = new FileInputStream(configFilePath);
            props.load(fis);
            fis.close();
            baseUrl = props.getProperty("API.url");
        } catch (IOException e) {
            e.printStackTrace();
            baseUrl = "https://api.eautantdone.com/api/"; // URL de base par défaut
        }
    }


    public static HttpResponse<String> postRequest(String url, Map<String, String> requestBody) throws ApiRequestException {

        // Créer un SSLContext sans vérification SSL
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Erreur lors de la création du SSLContext", e);
        }

        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Convertir le Map en JSON
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        System.out.println(baseUrl + url);
        // Créer une requête POST avec le corps JSON
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url))
                .header("Content-Type", "application/json");

        // Vérifier si l'utilisateur a un token et l'ajouter à l'en-tête si nécessaire
        String userToken = SessionManager.getInstance().getUserToken();
        if (userToken != null && !userToken.isEmpty()) {
            requestBuilder.header("Authorization", userToken);
        }

        HttpRequest request = requestBuilder
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

        // Créer un SSLContext sans vérification SSL
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Erreur lors de la création du SSLContext", e);
        }

        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Convertir le Map en JSON
        Gson gson = new Gson();
        String json = gson.toJson(requestBody);

        // Créer une requête PATCH avec le corps JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url))
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

        // Créer un SSLContext sans vérification SSL
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Erreur lors de la création du SSLContext", e);
        }

        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Créer une requête GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url))
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
    public static HttpResponse<String> deleteRequest(String url) throws ApiRequestException {

        // Créer un SSLContext sans vérification SSL
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Erreur lors de la création du SSLContext", e);
        }

        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Créer une requête DELETE
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url))
                .header("Authorization", SessionManager.getInstance().getUserToken()) // Ajouter le token d'authentification
                .DELETE()
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
