package com.example.atd.application;

import com.example.atd.ApiRequester;
import com.example.atd.adapter.SupportTypeAdapter;
import com.example.atd.adapter.TicketTypeAdapter;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Support;
import com.example.atd.model.Ticket;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

public class TicketManager {

    private static final String UNASSIGNED_TICKETS_ENDPOINT = "ticket/notassigned";
    private static final String ASSIGNED_TICKETS_ENDPOINT = "ticket/assigned";
    private ObservableList<Ticket> unassignedTickets = FXCollections.observableArrayList();
    private ObservableList<Ticket> assignedTickets = FXCollections.observableArrayList();
    private Stage primaryStage; // Déclaration de la variable primaryStage
    private ObservableList<Support> supportList;

    public TicketManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Assurez-vous que primaryStage n'est pas null avant de l'utiliser
        if (primaryStage == null) {
            throw new IllegalArgumentException("PrimaryStage cannot be null");
        }
        start();
    }

    public void start() {
        unassignedTickets = getTickets(UNASSIGNED_TICKETS_ENDPOINT);
        supportList = getSupportList();
        assignedTickets =  getTickets(ASSIGNED_TICKETS_ENDPOINT);

        Button sortBySeverityButton = new Button("Trier par sévérité");
        sortBySeverityButton.setOnAction(event -> sortBySeverity());

        Button sortByStatusButton = new Button("Trier par statut");
        sortByStatusButton.setOnAction(event -> sortByStatus());

        Button clearSortButton = new Button("Annuler le tri");
        clearSortButton.setOnAction(event -> clearSort());

        Button reloadButton = new Button("Reload");
        reloadButton.setOnAction(event -> {
            unassignedTickets.setAll(getTickets(UNASSIGNED_TICKETS_ENDPOINT));
            assignedTickets.setAll( getTickets(ASSIGNED_TICKETS_ENDPOINT)); // Recharge les tickets assignés
        });


        ListView<Ticket> unassignedListView = new ListView<>();
        unassignedListView.setItems(unassignedTickets);
        unassignedListView.setCellFactory(TicketCell.forListView(supportList, unassignedTickets, assignedTickets));

        ListView<Ticket> assignedListView = new ListView<>();
        assignedListView.setItems(assignedTickets);
        assignedListView.setCellFactory(TicketCell.forListView(supportList, unassignedTickets, assignedTickets));

        // Création des Labels pour les titres
        Label unassignedTicketsTitle = new Label("Tickets Non Assignés");
        Label assignedTicketsTitle = new Label("Tickets Assignés");

        // Créer une HBox pour contenir les boutons de tri
        HBox sortingButtons = new HBox(sortBySeverityButton, sortByStatusButton, clearSortButton, reloadButton);

        // Créer une VBox pour contenir la liste des tickets non assignés et les boutons de tri
        VBox unassignedTicketsLayout = new VBox(unassignedTicketsTitle, unassignedListView, sortingButtons);
        unassignedTicketsLayout.setPrefSize(400, 700);

        // Créer une VBox pour contenir la liste des tickets assignés
        VBox assignedTicketsLayout = new VBox(assignedTicketsTitle, assignedListView);
        assignedTicketsLayout.setPrefSize(400, 700);

        // Créer un HBox pour contenir les deux VBox (unassigned et assigned)
        HBox root = new HBox(unassignedTicketsLayout, assignedTicketsLayout);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Gestion des Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour trier les tickets par sévérité
    private void sortBySeverity() {
        Comparator<Ticket> severityComparator = Comparator.comparingInt(Ticket::getSeverity).reversed();
        FXCollections.sort(unassignedTickets, severityComparator);
        FXCollections.sort(assignedTickets, severityComparator); // Applique le tri sur les tickets assignés
    }

    // Méthode pour trier les tickets par statut
    private void sortByStatus() {
        Comparator<Ticket> statusComparator = Comparator.comparingInt(Ticket::getStatus).reversed();
        FXCollections.sort(unassignedTickets, statusComparator);
        FXCollections.sort(assignedTickets, statusComparator); // Applique le tri sur les tickets assignés
    }

    // Méthode pour annuler le tri
    private void clearSort() {
        // Recharger les tickets non triés
        Comparator<Ticket> idComparator = Comparator.comparingInt(Ticket::getId);
        FXCollections.sort(unassignedTickets, idComparator);
        FXCollections.sort(assignedTickets, idComparator); // Réinitialise le tri sur les tickets assignés
    }



    public ObservableList<Ticket> getTickets(String endpoint) {
        try {
            HttpResponse<String> response = ApiRequester.getRequest(endpoint);
            List<Ticket> ticketList = parseTicketList(response.body());
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

        // Désérialise l'objet JSON en utilisant un objet Wrapper
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("tickets");

        // Convertit chaque élément du tableau en un objet Ticket
        List<Ticket> ticketList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            Ticket ticket = gson.fromJson(jsonElement, Ticket.class);
            ticketList.add(ticket);
        }

        return ticketList;
    }

    private ObservableList<Support> getSupportList() {
        try {
            HttpResponse<String> response = ApiRequester.getRequest("user/support");
            List<Support> supportList = parseSupportList(response.body());
            return FXCollections.observableArrayList(supportList);
        } catch (ApiRequestException e) {
            handleApiRequestException(e);
        }
        return FXCollections.observableArrayList(); // Retourne une liste vide au lieu de null
    }

    private List<Support> parseSupportList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Enregistre l'adaptateur pour la classe Support
        gsonBuilder.registerTypeAdapter(Support.class, new SupportTypeAdapter());
        Gson gson = gsonBuilder.create();

        // Désérialise l'objet JSON en utilisant un objet Wrapper
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("supports");

        // Convertit chaque élément du tableau en un objet Support
        Type listType = new TypeToken<List<Support>>(){}.getType();
        List<Support> supportList = gson.fromJson(jsonArray, listType);

        return supportList;
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
