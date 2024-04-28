package com.example.atd.application;

import com.example.atd.model.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
            // Créez un Label pour afficher le message
            Label messageLabel = new Label(item.getDescription());
            // Appliquez le même style que celui utilisé dans addMessageToContainer
            messageLabel.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-border-radius: 10px; -fx-text-alignment: left;");
            setGraphic(messageLabel);
        }
    }
}

