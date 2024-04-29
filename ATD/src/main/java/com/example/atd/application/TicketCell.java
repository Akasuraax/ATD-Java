package com.example.atd.application;
import com.example.atd.ApiRequester;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Support;
import com.example.atd.model.Ticket;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class TicketCell extends ListCell<Ticket> {

    private ObservableList<Support> supportList;
    private ObservableList<Ticket> unassignedTickets;
    private ObservableList<Ticket> assignedTickets;
    public TicketCell(ObservableList<Support> supportList, ObservableList<Ticket> unassignedTickets, ObservableList<Ticket> assignedTickets) {
        this.supportList = supportList;
        this.unassignedTickets = unassignedTickets;
        this.assignedTickets = assignedTickets;

    }

    @Override
    protected void updateItem(Ticket item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            updateTicketText(item);
            setContextMenu(createContextMenu(item));
            updateSeverityStyle(item.getSeverity());
        }
    }

    private String getStatusDisplay(int status) {
        switch (status) {
            case 0:
                return "En attente";
            case 1:
                return "En cours";
            case 2:
                return "Terminé";
            default:
                return "Statut inconnu";
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
                updateTicketText(ticket);
                updateSeverityStyle(ticket.getSeverity());
                updateTicket(ticket);

            }
        });

        // Créer une ComboBox pour modifier le statut
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("En attente", "En cours", "Terminé");
        statusComboBox.setValue(getStatusDisplay(ticket.getStatus()));
        MenuItem menuItem2 = new MenuItem("Modifier le statut", statusComboBox);
        menuItem2.setOnAction(event -> {
            String selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();
            if (selectedStatus != null) {
                int statusValue = getStatusValue(selectedStatus);
                ticket.setStatus(statusValue);
                updateTicketText(ticket);
                updateTicket(ticket);
            }
        });

        Menu supportMenu = new Menu("Assigner à...");
        for (Support support : supportList) {
            MenuItem supportMenuItem = new MenuItem(support.getName() + " " + support.getForname());
            supportMenuItem.setOnAction(event -> {
                ticket.setSupport(support); // Attribuer le support sélectionné au ticket
                updateSupportTicket(ticket.getId(), support);
                updateTicketText(ticket);
            });
            supportMenu.getItems().add(supportMenuItem);
        }

        contextMenu.getItems().addAll(menuItem1, menuItem2, supportMenu);

        return contextMenu;
    }

    private int getStatusValue(String statusDisplay) {
        switch (statusDisplay) {
            case "En attente":
                return 0;
            case "En cours":
                return 1;
            case "Terminé":
                return 2;
            default:
                return -1; // Valeur par défaut si le statut n'est pas reconnu
        }
    }

    private void updateSeverityStyle(int severity) {
        switch (severity) {
            case 0:
                setStyle("-fx-text-fill: green;");
                break;
            case 1:
                setStyle("-fx-text-fill: #4c7500;");
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

    public static Callback<ListView<Ticket>, ListCell<Ticket>> forListView(ObservableList<Support> supportList, ObservableList<Ticket> unassignedTickets, ObservableList<Ticket> assignedTickets) {
        return param -> new TicketCell(supportList, unassignedTickets, assignedTickets);
    }



    public void updateSupportTicket(int id, Support support) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String url = "ticket/assign/" + id;

            Map<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(support.getId()));
            HttpResponse<String> response = ApiRequester.patchRequest(url, data);

            // Après avoir mis à jour le support du ticket, mettez à jour la liste de tickets
            updateTicketLists(id, support);

            Ticket updatedTicket = assignedTickets.stream()
                    .filter(ticket -> ticket.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (updatedTicket != null) {
                updateTicketText(updatedTicket);
            }
        } catch (ApiRequestException e) {
            // Gestion des erreurs
            System.err.println("Erreur lors de la requête API : " + e.getMessage());
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
            Map<String, String> data = getStringStringMap(ticket);

            HttpResponse<String> response = ApiRequester.patchRequest(url, data);
            updateTicketText(ticket);
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

    public void updateTicketLists(int ticketId, Support newSupport) {
        // Trouver le ticket dans les listes de tickets assignés et non assignés
        Ticket ticketToUpdate = null;
        for (Ticket ticket : unassignedTickets) {
            if (ticket.getId() == ticketId) {
                ticketToUpdate = ticket;
                break;
            }
        }
        if (ticketToUpdate == null) {
            for (Ticket ticket : assignedTickets) {
                if (ticket.getId() == ticketId) {
                    ticketToUpdate = ticket;
                    break;
                }
            }
        }

        // Si le ticket a été trouvé, mettez à jour son support et supprimez-le de la liste appropriée
        if (ticketToUpdate != null) {
            ticketToUpdate.setSupport(newSupport);
            // Si le ticket était dans la liste des tickets non assignés, le déplacer vers la liste des tickets assignés
            if (unassignedTickets.contains(ticketToUpdate)) {
                unassignedTickets.remove(ticketToUpdate);
                assignedTickets.add(ticketToUpdate);
            }
            // Si le ticket était déjà dans la liste des tickets assignés, simplement mettre à jour le support
            else if (assignedTickets.contains(ticketToUpdate)) {
                // Pas besoin de faire quoi que ce soit ici, car le support a déjà été mis à jour
            }
        }
    }

    private static Map<String, String> getStringStringMap(Ticket ticket) {
        Map<String, String> data = new HashMap<>();

        data.put("id", String.valueOf(ticket.getId()));
        data.put("title", ticket.getTitle());
        data.put("description", ticket.getDescription());
        data.put("status", String.valueOf(ticket.getStatus()));
        data.put("severity", String.valueOf(ticket.getSeverity()));
        data.put("archive", String.valueOf(ticket.isArchive()));
        data.put("createdAt", ticket.getCreatedAt().toString());
        data.put("updatedAt", ticket.getUpdatedAt().toString());
        data.put("problem", ticket.getProblem());
        return data;
    }

    private void updateTicketText(Ticket item) {
        String supportName = item.getSupport() != null ? " - Support: " + item.getSupport().getName() + " " + item.getSupport().getForname() : "";
        String statusDisplay = getStatusDisplay(item.getStatus());
        setText(item.getTitle() + " (ID: " + item.getId() + ") - Statut: " + statusDisplay + supportName);
    }

}
