package com.example.atd.application;

import com.example.atd.ApiRequester;
import com.example.atd.adapter.MessageTypeAdapter;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Message;
import com.example.atd.model.Ticket;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TicketDetailsContent {

    public VBox createContent(Ticket ticket) {
        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new javafx.geometry.Insets(10));

        Label titleLabel = new Label("Sujet du problème : " + ticket.getTitle());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-padding: 30px;");

        Separator separator = new Separator();

        // Utilisation d'un Text pour la description du ticket
        Label descriptionLabel = new Label("Description :");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 0px 0px 30px");

        Text descriptionText = new Text(ticket.getDescription());
        descriptionText.setWrappingWidth(360); // Définit la largeur maximale avant le retour à la ligne
        descriptionText.setStyle("-fx-padding: 50px; -fx-background-color: #f0f0f0;");

        // Scroll pour l'historique des msgs
        Label scrollLabel = new Label("Historique des messages :");
        scrollLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 0px 0px 30px");

        ScrollPane descriptionScrollPane = new ScrollPane(descriptionText);
        descriptionScrollPane.setFitToWidth(true);
        descriptionScrollPane.setFitToHeight(true);

        // Définition d'une hauteur maximale pour le ScrollPane
        descriptionScrollPane.setMaxHeight(400);

        ScrollPane messageScrollPane = createMessageContainer(getMessages(ticket.getId()));

        content.getChildren().addAll(titleLabel, separator, descriptionLabel, descriptionScrollPane,  scrollLabel, messageScrollPane);
        return content;
    }

    private ScrollPane createMessageContainer(ObservableList<Message> messages) {
        VBox messageContainer = new VBox();
        messageContainer.setSpacing(5);
        messageContainer.setPadding(new javafx.geometry.Insets(10));

        for (Message message : messages) {
            Label messageLabel = new Label(message.getUserWhoSendTheMessage().getName() + " " + message.getUserWhoSendTheMessage().getForname() + " : " + message.getDescription());
            messageContainer.getChildren().add(messageLabel);
            messageLabel.setStyle("-fx-background-color:#F85866; -fx-text-fill: #F1F1F1; -fx-padding: 8px; -fx-border-width: 1px;"); // Ajoutez une bordure et un padding au VBox
        }

        // Création du ScrollPane et ajout du VBox comme son contenu
        ScrollPane scrollPane = new ScrollPane(messageContainer);
        scrollPane.setFitToWidth(true); // Ajuste le ScrollPane pour qu'il s'adapte à la largeur de son contenu
        scrollPane.setPrefSize(500, 200); // Définit la taille préférée du ScrollPane

        // Appliquer un style CSS au ScrollPane
        scrollPane.setStyle("-fx-background-color: #ffffff;"); // Exemple de couleur de fond

        return scrollPane; // Retourne le ScrollPane
    }



    public ObservableList<Message> getMessages(int ticketId) {
        try {
            String endpoint = "ticket/" + ticketId + "/messages";
            HttpResponse<String> response = ApiRequester.getRequest(endpoint);

            List<Message> messageList = parseMessageList(response.body());
            return FXCollections.observableArrayList(messageList);
        } catch (ApiRequestException e) {
            handleApiRequestException(e);
        }
        return FXCollections.observableArrayList();
    }

    private List<Message> parseMessageList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageTypeAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("messages");

        List<Message> messageList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            Message message = gson.fromJson(jsonElement, Message.class);
            messageList.add(message);
        }

        return messageList;
    }

    private void handleApiRequestException(ApiRequestException e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la récupération des données");
            alert.showAndWait();
        });
    }

}
