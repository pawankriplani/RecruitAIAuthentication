package com.example.authentication.dto;

public class UnlockAccountRequest {
    private String email;
    private String status;

    public UnlockAccountRequest() {
    }

    public UnlockAccountRequest(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
