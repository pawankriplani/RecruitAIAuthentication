package com.example.authentication.service;

import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.event.AccountApprovalData;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<Void> sendUserRegistrationNotification(UserRegistrationData data);
    CompletableFuture<Void> sendAccountApprovedNotification(AccountApprovalData data);
}
