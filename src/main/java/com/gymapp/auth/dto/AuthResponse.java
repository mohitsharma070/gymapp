package com.gymapp.auth.dto;

public class AuthResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String token;

    public AuthResponse(Long userId, String fullName, String email, String token) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
