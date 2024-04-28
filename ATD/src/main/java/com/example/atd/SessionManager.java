package com.example.atd;

import com.example.atd.model.User;

public class SessionManager {
    private static SessionManager instance;
    private String userToken;
    private User user;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUserToken(String token) {
        this.userToken = token;
    }

    public String getUserToken() {
        return userToken;
    }

    public void logout() {
        instance = null;
        // Et une autre pour stocker le token d'authentification
        userToken = null;
        user = null;
        // Vous pouvez également effacer d'autres informations de session ici
    }
}
