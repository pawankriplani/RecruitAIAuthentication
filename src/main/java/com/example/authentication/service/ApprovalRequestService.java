package com.example.authentication.service;

import com.example.authentication.model.User;
import com.example.authentication.model.Role;
import com.example.authentication.model.AccountApprovalRequest;

public interface ApprovalRequestService {
    AccountApprovalRequest createApprovalRequest(User user);
    void approveManagerAccount(Integer userId);
    void rejectManagerAccount(Integer userId, String reason);
}
