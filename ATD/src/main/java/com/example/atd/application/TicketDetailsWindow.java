package com.example.atd.application;

import com.example.atd.model.Ticket;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicketDetailsWindow extends Stage {

    public TicketDetailsWindow(Ticket ticket) {
        setTitle("Détails du Ticket");

        TicketDetailsContent contentCreator = new TicketDetailsContent();
        VBox content = contentCreator.createContent(ticket);

        Scene scene = new Scene(content, 600, 800); // Ajustez la taille de la scène si nécessaire
        setScene(scene);
    }
}
