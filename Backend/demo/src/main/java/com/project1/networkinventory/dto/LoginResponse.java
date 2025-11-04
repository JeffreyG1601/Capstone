package com.project1.networkinventory.dto;

import com.project1.networkinventory.model.User;

public class LoginResponse {
    private String token;
    private User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
        // hide password
        if (this.user != null) this.user.setPasswordHash(null);
    }

    public String getToken() { return token; }
    public User getUser() { return user; }
}
