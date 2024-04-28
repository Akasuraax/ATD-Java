package com.example.atd.application;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import com.example.atd.model.Ticket;

public class TicketCellSupport extends ListCell<Ticket> {

    public TicketCellSupport() {
        // Constructeur sans paramètres
    }

    @Override
    protected void updateItem(Ticket item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            // Utilisez le nom du support pour afficher dans la cellule
            String supportName = item.getSupport() != null ? item.getSupport().getName() : "Non assigné";
            setText(item.getTitle() + " (ID: " + item.getId() + ") - Statut: " + item.getStatus() + " - Support: " + supportName);
            // Mettre à jour le style en fonction de la sévérité
            updateSeverityStyle(item.getSeverity());
        }
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
}
