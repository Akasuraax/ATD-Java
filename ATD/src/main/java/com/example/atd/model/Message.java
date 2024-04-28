package com.example.atd.model;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String description;
    private boolean archive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int idUser;
    private int idTicket;
    private UserDetails userWhoSendTheMessage;

    // Constructeur par défaut
    public Message() {
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public UserDetails getUserWhoSendTheMessage() {
        return userWhoSendTheMessage;
    }

    public void setUserWhoSendTheMessage(UserDetails userWhoSendTheMessage) {
        this.userWhoSendTheMessage = userWhoSendTheMessage;
    }
}
