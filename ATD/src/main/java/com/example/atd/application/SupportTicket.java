package com.example.atd.application;

import com.example.atd.ApiRequester;
import com.example.atd.SessionManager;
import com.example.atd.adapter.TicketTypeAdapter;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Message;
import com.example.atd.model.Ticket;
import com.google.gson.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SupportTicket extends Application {

    private ListView<Ticket> ticketListView;
    private TextArea ticketDetailsArea;
    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        ticketListView = new ListView<>();
        ticketDetailsArea = new TextArea();

        // Initialiser la ListView avec l'ObservableList
        ticketListView.setItems(tickets);

        // Récupérer les tickets et les ajouter à l'ObservableList
        int userId = SessionManager.getInstance().getUser().getUserDetails().getId();
        String endpoint = "user/" + userId + "/tickets"; // Remplacez par votre endpoint réel
        tickets.addAll(getTickets(endpoint));

        // Configuration de la sélection de ticket
        ticketListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ticketDetailsArea.clear();
                ticketDetailsArea.appendText("Titre: " + newSelection.getTitle() + "\n");
                for (Message message : newSelection.getMessages()) {
                    ticketDetailsArea.appendText("Message: " + message.getContent() + "\n");
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setLeft(ticketListView);
        root.setRight(ticketDetailsArea);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Gestion des Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Ticket> getTickets(String endpoint) {
        try {
            HttpResponse<String> response = ApiRequester.getRequest(endpoint);
            List<Ticket> ticketList = parseTicketList(response.body());
            System.out.println(response.body());
            return FXCollections.observableArrayList(ticketList);
        } catch (ApiRequestException e) {
            handleApiRequestException(e);
        }
        return FXCollections.observableArrayList(); // Retourne une liste vide au lieu de null
    }

    private List<Ticket> parseTicketList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Ticket.class, new TicketTypeAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("tickets");

        List<Ticket> ticketList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            Ticket ticket = gson.fromJson(jsonElement, Ticket.class);
            ticketList.add(ticket);
        }

        return ticketList;
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
