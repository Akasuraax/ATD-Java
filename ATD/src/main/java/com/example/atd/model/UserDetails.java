package com.example.atd.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.List;

public class UserDetails {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("forname")
    private String forname;
    @SerializedName("email")
    private String email;
    @SerializedName("status")
    private int status;
    @SerializedName("created_at")
    private LocalDateTime createdAt;
    @SerializedName("updated_at")
    private LocalDateTime updatedAt;

    public UserDetails() {}

    public UserDetails(int id, String name, String forname, String email, int status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.forname = forname;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForname(String forname) {
        this.forname = forname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getId() {
        return id;
    }

    public String getForname() {
        return forname;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }
}
