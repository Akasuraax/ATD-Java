package com.example.atd.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private UserDetails user;

    public User(String token, UserDetails user) {
        this.token = token;
        this.user = user;
    }
    // Getters et setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetails getUserDetails() {
        return user;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.user = userDetails;
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", userDetails=" + user +
                '}';
    }
}