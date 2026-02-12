package com.example.authentication.service.impl;

import com.example.authentication.event.NotificationEvent;
import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.event.AccountApprovalData;
import com.example.authentication.service.NotificationService;
import com.example.authentication.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class AsyncNotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncNotificationServiceImpl.class);

    @Value("${notification.service.url}")
    private String notificationUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AsyncNotificationServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> sendUserRegistrationNotification(UserRegistrationData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Attempting to send notification for user: {}", data.getUsername());
                NotificationEvent<UserRegistrationData> event = new NotificationEvent<>();
                event.setEventType(Constants.EVENT_USER_REGISTERED);
                event.setTimestamp(Instant.now().toString());
                event.setData(data);

                String response = restTemplate.postForObject(notificationUrl+"/api/notifications/process", event, String.class);
                logger.info("Successfully sent user registration notification for user: {} with RMG email: {}. Response: {}", 
                    data.getUsername(), data.getRmgEmail(), response);
            } catch (Exception e) {
                logger.error("Failed to send user registration notification for user: {}. Error: {}", 
                    data.getUsername(), e.getMessage());
                logger.debug("Detailed error:", e);
                if (e.getCause() != null) {
                    logger.error("Root cause: {}", e.getCause().getMessage());
                }
                throw new CompletionException(e);
            }
        });
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> sendAccountApprovedNotification(AccountApprovalData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Attempting to send account approved notification for user: {}", data.getUsername());
                String eventType="";
                if(data.getApproved()==true) {
                	eventType= Constants.EVENT_ACCOUNT_APPROVED;
                }else {
                	eventType=Constants.EVENT_ACCOUNT_REJECTED;
                }
                NotificationEvent<AccountApprovalData> event = new NotificationEvent<>(
                		eventType,
                    Instant.now().toString(),
                    data
                );

                String response = restTemplate.postForObject(notificationUrl+"/api/notifications/process", event, String.class);
                logger.info("Successfully sent account approved notification for user: {} approved by: {}. Response: {}", 
                    data.getUsername(), data.getApprovedBy(), response);
            } catch (Exception e) {
                logger.error("Failed to send account approved notification for user: {}. Error: {}", 
                    data.getUsername(), e.getMessage());
                logger.debug("Detailed error:", e);
                if (e.getCause() != null) {
                    logger.error("Root cause: {}", e.getCause().getMessage());
                }
                throw new CompletionException(e);
            }
        });
    }
}
