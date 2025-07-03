package com.example.authentication.event;

public class AccountApprovalData {
    private String userId;
    private String username;
    private String email;
    private String approvedBy;
    private String messages;
    private Boolean approval;

    public AccountApprovalData(String userId, String username, String email, String approvedBy, String messages, Boolean approval) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.approvedBy = approvedBy;
        this.messages = messages;
        this.approval = approval;
    }

    // Default constructor
    public AccountApprovalData() {
    }

    // Getters and setters
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }
}
