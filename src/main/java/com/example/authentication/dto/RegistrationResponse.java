package com.example.authentication.dto;

public class RegistrationResponse {
    private String message;
    private Integer userId;
    private boolean requiresApproval;

    public RegistrationResponse(String message, Integer userId, boolean requiresApproval) {
        this.message = message;
        this.userId = userId;
        this.requiresApproval = requiresApproval;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }
}
