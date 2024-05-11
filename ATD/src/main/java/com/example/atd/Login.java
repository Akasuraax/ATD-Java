package com.example.atd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        changeToLoginScene();
    }

    public static void changeToLoginScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Login.class.getResource("login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setMainStage(mainStage);
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(Login.class.getResource("/css/styleLogin.css").toExternalForm());
        mainStage.setTitle("Login Page");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
