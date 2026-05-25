package com.example.realtimechat.dto;

public class AuthResponse {

    private Long userId;
    private String fullName;
    private String username;
    private String message;

    public AuthResponse(Long userId, String fullName, String username, String message) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
