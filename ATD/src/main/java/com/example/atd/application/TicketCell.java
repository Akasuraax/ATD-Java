package com.example.atd.application;

import com.example.atd.model.Ticket;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TicketCell extends ListCell<Ticket> {

    @Override
    protected void updateItem(Ticket item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getTitle() + " (ID: " + item.getId() + ")");
        }
    }

    public static Callback<ListView<Ticket>, ListCell<Ticket>> forListView() {
        return param -> new TicketCell();
    }
}
