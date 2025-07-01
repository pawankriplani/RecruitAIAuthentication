package com.example.authentication.service;

import com.example.authentication.event.UserRegistrationData;
import java.util.concurrent.CompletableFuture;

public interface PubSubService {
    CompletableFuture<Void> publishUserRegistrationEvent(UserRegistrationData data);
}
