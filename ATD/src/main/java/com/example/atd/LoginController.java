package com.example.atd;

import com.example.atd.adapter.UserDetailsTypeAdapter;
import com.example.atd.application.SupportTicket;
import com.example.atd.application.TicketManager;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.User;
import com.example.atd.model.UserDetails;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    private Stage mainStage; // Variable pour stocker le Stage

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

            HttpResponse<String> response = ApiRequester.postRequest(url, data);
            if(response.statusCode() == 200) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(UserDetails.class, new UserDetailsTypeAdapter())
                        .create();
                String body = response.body();
                // Conversion de la chaîne JSON en un objet User
                JsonElement userElement = JsonParser.parseString(body).getAsJsonObject().get("user");
                String token = JsonParser.parseString(body).getAsJsonObject().get("token").getAsString();
                SessionManager.getInstance().setUserToken(token);

                JsonObject userObject = userElement.getAsJsonObject();
                JsonArray rolesArray = userObject.getAsJsonArray("roles");
                int roleId = -1;
                for (JsonElement roleElement : rolesArray) {
                    JsonObject roleObject = roleElement.getAsJsonObject();
                    // Récupérer l'ID du rôle
                    roleId = roleObject.get("id").getAsInt();
                }

                if(roleId != 5 && roleId != 6) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous n'avez pas le droit d'acceder a l'application");
                        alert.showAndWait();
                    });
                } else {

                    UserDetails userDetails = gson.fromJson(userElement, UserDetails.class);

                    User user = new User(token, userDetails);
                    SessionManager.getInstance().setUser(user);
                    SessionManager.getInstance().setUserToken(token);


                    if(roleId == 6 ) {
                        System.out.println(6);
                        Platform.runLater(() -> {
                            new TicketManager(mainStage);
                        });
                    } else {
                        System.out.println(5);
                        Platform.runLater(() -> {
                            new SupportTicket().start(mainStage);
                        });
                    }
                }
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Identifiant incorrect");
                    alert.showAndWait();
                });
            }
        } catch (ApiRequestException e) {
            // Afficher un message d'erreur à l'utilisateur
            System.err.println("Erreur lors de la requête API : " + e.getMessage());
            // Pour une application JavaFX, vous pouvez utiliser un dialogue pour afficher l'erreur
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }
}
