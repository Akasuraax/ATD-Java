package com.example.atd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        // Passage du Stage principal au contrôleur
        controller.setMainStage(primaryStage);
        Scene scene = new Scene(root, 300, 275);

        scene.getStylesheets().add(getClass().getResource("/css/styleLogin.css").toExternalForm());

        // Créer le MenuBar et le MenuItem pour le bouton de déconnexion
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Fichier");
        MenuItem logoutMenuItem = new MenuItem("Déconnexion");
        menu.getItems().add(logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Définir l'action du MenuItem
        logoutMenuItem.setOnAction(event -> {
            // Ici, vous pouvez ajouter le code pour déconnecter l'utilisateur
            // Par exemple, fermer la fenêtre actuelle et ouvrir la fenêtre de connexion
            primaryStage.close();
            // Ouvrez la fenêtre de connexion ici
        });

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
