package com.example.atd.application;

import com.example.atd.SessionManager;
import com.example.atd.model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TicketManager {

    private ObservableList<Ticket> unassignedTickets = FXCollections.observableArrayList();
    private ObservableList<Ticket> assignedTickets = FXCollections.observableArrayList();
    private Stage primaryStage; // Déclaration de la variable primaryStage

    public TicketManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Assurez-vous que primaryStage n'est pas null avant de l'utiliser
        if (primaryStage == null) {
            throw new IllegalArgumentException("PrimaryStage cannot be null");
        }
        start();
    }

    public void start() {
        unassignedTickets.add(new Ticket(1, "Ticket 1", false));
        unassignedTickets.add(new Ticket(2, "Ticket 2", false));
        assignedTickets.add(new Ticket(3, "Ticket 3", true));

        ListView<Ticket> unassignedListView = new ListView<>();
        unassignedListView.setItems(unassignedTickets);
        unassignedListView.setCellFactory(param -> new TicketCell());

        ListView<Ticket> assignedListView = new ListView<>();
        assignedListView.setItems(assignedTickets);
        assignedListView.setCellFactory(param -> new TicketCell());

        // Création du ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        // Création des MenuItem et ajout au ContextMenu
        MenuItem menuItem1 = new MenuItem("Option 1");
        MenuItem menuItem2 = new MenuItem("Option 2");
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        // Association du ContextMenu aux ListView
        unassignedListView.setContextMenu(contextMenu);
        assignedListView.setContextMenu(contextMenu);

        // Ajout des écouteurs d'événements aux MenuItem si nécessaire
        menuItem1.setOnAction(event -> {
            // Code à exécuter lorsque l'utilisateur sélectionne "Option 1"
            System.out.println("Option 1 sélectionnée");
        });

        menuItem2.setOnAction(event -> {
            // Code à exécuter lorsque l'utilisateur sélectionne "Option 2"
            System.out.println("Option 2 sélectionnée");
        });

        HBox root = new HBox(unassignedListView, assignedListView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Gestion de Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println(SessionManager.getInstance().getUserToken());
    }
}
