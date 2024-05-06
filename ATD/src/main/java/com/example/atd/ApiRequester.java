package com.example.atd;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

import com.example.atd.exception.ApiRequestException;
import com.google.gson.Gson;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

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
            baseUrl = "http://api.eautantdone.com/api/"; // URL de base par défaut
        }
    }

    public static SSLContext createSSLContext(String keystorePath, String keystorePassword) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream(keystorePath);
        ks.load(fis, keystorePassword.toCharArray());
        fis.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }



    public static HttpResponse<String> postRequest(String url, Map<String, String> requestBody) throws ApiRequestException {
        // Utiliser le SSLContext personnalisé
        SSLContext sslContext = null;
        try {
            sslContext = createSSLContext("E:\\Dev\\Projets\\PA\\ATD-Java\\mykeystore.jks", "eautantdone");
        } catch (Exception e) {
            // Gérer l'exception
            System.err.println("Erreur lors de la création du SSLContext : " + e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Erreur lors de la création du SSLContext : " + e.getMessage());
        }
        // Créer un client HTTP avec le SSLContext personnalisé
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

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
        if (userToken!= null &&!userToken.isEmpty()) {
            requestBuilder.header("Authorization", userToken);
        }

        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (java.net.ConnectException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Erreur de connexion : " + e.getMessage());
        } catch (Exception e) {
            // Attraper toutes les autres exceptions
            System.err.println("Erreur inattendue lors de la requête API : " + e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Erreur inattendue lors de la requête API : " + e.getMessage());
        }
    }

    public static HttpResponse<String> patchRequest(String url, Map<String, String> requestBody) throws ApiRequestException {
        // Utiliser le SSLContext personnalisé
        SSLContext sslContext = null;
        try {
            sslContext = createSSLContext("E:\\Dev\\Projets\\PA\\ATD-Java\\mykeystore.jks", "eautantdone");
        } catch (Exception e) {
            // Gérer l'exception
            e.printStackTrace();
        }

        // Créer un client HTTP avec le SSLContext personnalisé
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

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
        // Utiliser le SSLContext personnalisé
        SSLContext sslContext = null;
        try {
            sslContext = createSSLContext("E:\\Dev\\Projets\\PA\\ATD-Java\\mykeystore.jks", "eautantdone");
        } catch (Exception e) {
            // Gérer l'exception
            e.printStackTrace();
        }

        // Créer un client HTTP avec le SSLContext personnalisé
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

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
        // Utiliser le SSLContext personnalisé
        SSLContext sslContext = null;
        try {
            sslContext = createSSLContext("E:\\Dev\\Projets\\PA\\ATD-Java\\mykeystore.jks", "eautantdone");
        } catch (Exception e) {
            // Gérer l'exception
            e.printStackTrace();
        }

        // Créer un client HTTP avec le SSLContext personnalisé
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

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
