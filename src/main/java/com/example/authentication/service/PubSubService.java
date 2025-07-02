package com.example.authentication.service;

import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.event.AccountApprovalData;
import com.example.authentication.event.PubSubEvent;
import java.util.concurrent.CompletableFuture;

public interface PubSubService {
    CompletableFuture<Void> publishUserRegistrationEvent(UserRegistrationData data);
    CompletableFuture<Void> publishAccountApprovedEvent(AccountApprovalData data);
}
