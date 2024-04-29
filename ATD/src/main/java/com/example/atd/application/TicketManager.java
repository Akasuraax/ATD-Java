package com.example.atd.application;

import com.example.atd.ApiRequester;
import com.example.atd.Login;
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
import javafx.scene.layout.BorderPane;
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

    private ObservableList<Ticket> completedTickets = FXCollections.observableArrayList();


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
        assignedTickets = getTickets(ASSIGNED_TICKETS_ENDPOINT);

        filterCompletedTickets();

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

        ListView<Ticket> completedListView = new ListView<>();
        completedListView.setItems(completedTickets);
        completedListView.setCellFactory(TicketCell.forListView(supportList, unassignedTickets, assignedTickets));

        Label unassignedTicketsTitle = new Label("Tickets Non Assignés");
        Label assignedTicketsTitle = new Label("Tickets Assignés");
        Label completedTicketsTitle = new Label("Tickets Terminés");

        HBox sortingButtons = new HBox(sortBySeverityButton, sortByStatusButton, clearSortButton, reloadButton);

        VBox unassignedTicketsLayout = new VBox(unassignedTicketsTitle, unassignedListView, sortingButtons);

        VBox assignedTicketsLayout = new VBox(assignedTicketsTitle, assignedListView);

        VBox completedTicketsLayout = new VBox(completedTicketsTitle, completedListView);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Fichier");
        MenuItem logoutMenuItem = new MenuItem("Déconnexion");
        menu.getItems().add(logoutMenuItem);
        menuBar.getMenus().add(menu);

        logoutMenuItem.setOnAction(event -> {
            primaryStage.close();
            try {
                Login.changeToLoginScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Utiliser un BorderPane pour organiser les éléments
        BorderPane root = new BorderPane();
        root.setTop(menuBar); // Ajouter le MenuBar en haut
        root.setCenter(new HBox(unassignedTicketsLayout, assignedTicketsLayout, completedTicketsLayout)); // Ajouter les VBox au centre

        Scene scene = new Scene(root, 1800, 800);

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
        System.out.println(json);
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

    private void filterCompletedTickets() {
        // Filter unassigned tickets
        assignedTickets.stream()
                .filter(ticket -> ticket.getStatus() == 2)
                .forEach(completedTickets::add);
        // Filter assigned tickets and remove completed ones
        assignedTickets.removeIf(ticket -> ticket.getStatus() == 2);
    }

}
