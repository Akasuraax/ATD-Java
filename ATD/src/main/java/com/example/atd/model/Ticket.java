package com.example.atd.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("status")
    private int status;
    @SerializedName("severity")
    private int severity;
    @SerializedName("archive")
    private boolean archive;
    @SerializedName("createdAt")
    private LocalDateTime createdAt;
    @SerializedName("updatedAt")
    private LocalDateTime updatedAt;
    @SerializedName("problem")
    private String problem;
    @SerializedName("support")
    private Support support;
    @SerializedName("support")
    private List<Message> messages;



    public Ticket(int id, String title, String description, int status, int severity, boolean archive, LocalDateTime createdAt, LocalDateTime updatedAt, String problem, Support support) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.severity = severity;
        this.archive = archive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.problem = problem;
        this.support = support;
        this.messages = new ArrayList<>();
    }

    public Ticket() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
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

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
