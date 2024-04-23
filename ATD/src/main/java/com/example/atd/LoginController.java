package com.example.atd;

import com.example.atd.adapter.UserDetailsTypeAdapter;
import com.example.atd.application.TicketManager;
import com.example.atd.model.User;
import com.example.atd.model.UserDetails;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    private Stage mainStage; // Variable pour stocker le Stage

    // Constructeur sans arguments requis par FXMLLoader
    public LoginController() {
        // Initialisation si nécessaire
    }

    // Méthode pour définir le Stage après l'initialisation du contrôleur
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");

            String url = "logIn";

            Map<String, String> data = new HashMap<>();
            data.put("email", username);
            data.put("password", password);

            String response = ApiRequester.postRequest(url,data);
            System.out.println(response);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(UserDetails.class, new UserDetailsTypeAdapter())
                    .create();

            // Conversion de la chaîne JSON en un objet User
            JsonElement userElement = JsonParser.parseString(response).getAsJsonObject().get("user");
            String token = JsonParser.parseString(response).getAsJsonObject().get("token").getAsString();

            UserDetails userDetails = gson.fromJson(userElement, UserDetails.class);

            User user = new User(token, userDetails);

            Platform.runLater(() -> {
                TicketManager ticketManager = new TicketManager(mainStage);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
