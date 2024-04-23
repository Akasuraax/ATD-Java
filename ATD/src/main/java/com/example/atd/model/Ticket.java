package com.example.atd.model;

public class Ticket {
    private int id;
    private String title;
    private boolean isAssigned;

    public Ticket(int id, String title, boolean isAssigned) {
        this.id = id;
        this.title = title;
        this.isAssigned = isAssigned;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
