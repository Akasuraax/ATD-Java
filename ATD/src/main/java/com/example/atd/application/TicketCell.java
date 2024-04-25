package com.example.atd.application;

import com.example.atd.ApiRequester;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Support;
import com.example.atd.model.Ticket;
import com.example.atd.model.User;
import com.example.atd.model.UserDetails;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javafx.util.Callback;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketCell extends ListCell<Ticket> {

    private ObservableList<Support> supportList;

    public TicketCell(ObservableList<Support> supportList) {
        this.supportList = supportList;
    }
    @Override
    protected void updateItem(Ticket item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getTitle() + " (ID: " + item.getId() + ") - Statut: " + item.getStatus());
            setContextMenu(createContextMenu(item));
            updateSeverityStyle(item.getSeverity());
        }
    }

    private ContextMenu createContextMenu(Ticket ticket) {
        ContextMenu contextMenu = new ContextMenu();

        // Créer une ComboBox pour modifier la gravité
        ComboBox<Integer> severityComboBox = new ComboBox<>();
        severityComboBox.getItems().addAll(0, 1, 2, 3, 4, 5);
        severityComboBox.setValue(ticket.getSeverity()); // Définir la valeur initiale de la ComboBox
        MenuItem menuItem1 = new MenuItem("Modifier la gravité", severityComboBox);
        menuItem1.setOnAction(event -> {
            Integer selectedSeverity = severityComboBox.getSelectionModel().getSelectedItem();
            if (selectedSeverity != null) {
                ticket.setSeverity(selectedSeverity);
                setText(ticket.getTitle() + " (ID: " + ticket.getId() + ") - Statut: " + ticket.getStatus());
                updateSeverityStyle(ticket.getSeverity());
                //updateTicket(ticket);

            }
        });

        // Créer une ComboBox pour modifier le statut
        ComboBox<Integer> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(0, 1, 2, 3);
        statusComboBox.setValue(ticket.getStatus()); // Définir la valeur initiale de la ComboBox
        MenuItem menuItem2 = new MenuItem("Modifier le statut", statusComboBox);
        menuItem2.setOnAction(event -> {
            Integer selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();
            if (selectedStatus != null) {
                ticket.setStatus(selectedStatus);
                setText(ticket.getTitle() + " (ID: " + ticket.getId() + ") - Statut: " + ticket.getStatus());
                //updateTicket(ticket);
            }
        });

        Menu supportMenu = new Menu("Assigner à...");
        for (Support support : supportList) {
            MenuItem supportMenuItem = new MenuItem(support.getName() + " " + support.getForname());
            supportMenuItem.setOnAction(event -> {
                ticket.setSupport(support); // Attribuer le support sélectionné au ticket
                setText(ticket.getTitle() + " (ID: " + ticket.getId() + ") - Statut: " + ticket.getStatus());
            });
            supportMenu.getItems().add(supportMenuItem);
        }

        contextMenu.getItems().addAll(menuItem1, menuItem2, supportMenu);

        return contextMenu;
    }

    private void updateSeverityStyle(int severity) {
        switch (severity) {
            case 0:
                setStyle("-fx-text-fill: green;");
                break;
            case 1:
                setStyle("-fx-text-fill: yellow;");
                break;
            case 2:
                setStyle("-fx-text-fill: orange;");
                break;
            case 3:
                setStyle("-fx-text-fill: red;");
                break;
            case 4:
                setStyle("-fx-text-fill: darkred;");
                break;
            case 5:
                setStyle("-fx-text-fill: maroon;");
                break;
            default:
                setStyle(null); // Réinitialiser le style si la sévérité n'est pas dans la plage valide
                break;
        }
    }

    public static Callback<ListView<Ticket>, ListCell<Ticket>> forListView(ObservableList<Support> supportList) {
        return param -> new TicketCell(supportList);
    }

    public void updateSupportTicket(int id, Support support) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");

            String url = "logIn";

            Map<String, String> data = new HashMap<>();

            HttpResponse<String> response = ApiRequester.postRequest(url, data);
        } catch (
                ApiRequestException e) {
            // Afficher un message d'erreur à l'utilisateur
            System.err.println("Erreur lors de la requête API : " + e.getMessage());
            // Pour une application JavaFX, vous pouvez utiliser un dialogue pour afficher l'erreur
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }

        public void updateTicket (Ticket ticket){
            try {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");

                String url = "ticket/" + ticket.getId();

                Map<String, String> data = new HashMap<>();
                Gson gson = new Gson();
                String ticketJson = gson.toJson(ticket);
                data.put("ticket", ticketJson);

                HttpResponse<String> response = ApiRequester.patchRequest(url, data);
                System.out.println(response);
            } catch (
                    ApiRequestException e) {
                // Afficher un message d'erreur à l'utilisateur
                System.err.println("Erreur lors de la requête API : " + e.getMessage());
                // Pour une application JavaFX, vous pouvez utiliser un dialogue pour afficher l'erreur
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de connexion");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        }
}
