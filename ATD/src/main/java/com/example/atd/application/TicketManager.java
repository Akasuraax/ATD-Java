package com.example.atd.application;

import com.example.atd.model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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

        HBox root = new HBox(unassignedListView, assignedListView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Gestion de Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
