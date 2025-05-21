package com.example.forum.dto;

public class LoginResponse {
    private Long userId;
    private String token;

    public LoginResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}