package com.example.atd;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Ici, vous devriez vérifier les identifiants de l'utilisateur.
        // Pour cet exemple, nous allons simplement afficher les valeurs dans la console.
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Après la vérification, vous pouvez rediriger l'utilisateur vers une autre page.
        // Par exemple, si les identifiants sont corrects :
        // Parent root = FXMLLoader.load(getClass().getResource("nextPage.fxml"));
        // primaryStage.setScene(new Scene(root, 300, 275));
    }
}
