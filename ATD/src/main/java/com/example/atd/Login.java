package com.example.atd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));

        Scene scene = new Scene(root, 300, 275);

        scene.getStylesheets().add(getClass().getResource("/css/styleLogin.css").toExternalForm());

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
