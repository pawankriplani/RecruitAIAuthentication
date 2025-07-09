package com.example.authentication.dto;

public class UnlockAccountRequest {
    private String email;

    public UnlockAccountRequest() {
    }

    public UnlockAccountRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
