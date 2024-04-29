package com.example.atd.application;

import com.example.atd.model.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MessageCell extends ListCell<Message> {
    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Créez un HBox pour contenir le nom de l'utilisateur et le message
            HBox messageContainer = new HBox();

            // Créez un Label pour afficher le nom de l'utilisateur
            Label userLabel = new Label(item.getUserWhoSendTheMessage().getName() + " " + item.getUserWhoSendTheMessage().getForname());
            userLabel.setStyle("-fx-font-weight: bold; -fx-padding-right: 5px;");

            // Créez un Label pour afficher le message
            Label messageLabel = new Label(item.getDescription());
            messageLabel.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-border-radius: 10px; -fx-text-alignment: left;");

            // Ajoutez le nom de l'utilisateur et le message au HBox
            messageContainer.getChildren().addAll(userLabel, messageLabel);

            // Définissez le HBox comme le graphique de la cellule
            setGraphic(messageContainer);
        }
    }
}
