package com.example.atd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");

            String url = "http://10.188.41.155:8000/api/logIn";

            Map<String, String> data = new HashMap<>();
            data.put("email", username);
            data.put("password", password);

            String response = ApiRequester.postRequest(url,data);

            Gson gson = new Gson();
            User user = gson.fromJson(response, User.class);
            System.out.println(user.toString());
            System.out.println("Token: " + user.getToken());

            System.out.println("Token: " + user.getToken());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
