package com.example.authentication.event;

public class UserRegistrationData {
    private String userId;
    private String username;
    private String email;
    private String role;
    private String status;
    private String rmgEmail;

    public UserRegistrationData(String userId, String username, String email, String role, String status, String rmgEmail) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.rmgEmail = rmgEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRmgEmail() {
        return rmgEmail;
    }

    public void setRmgEmail(String rmgEmail) {
        this.rmgEmail = rmgEmail;
    }
}
