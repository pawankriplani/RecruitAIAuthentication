package com.example.authentication.service.impl;

import com.example.authentication.event.PubSubEvent;
import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.event.AccountApprovalData;
import com.example.authentication.service.PubSubService;
import com.example.authentication.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class PubSubServiceImpl implements PubSubService {
    private static final Logger logger = LoggerFactory.getLogger(PubSubServiceImpl.class);

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;
    private final String userRegistrationTopic;
    private final String accountApprovedTopic;

    @Autowired
    public PubSubServiceImpl(
            PubSubTemplate pubSubTemplate,
            ObjectMapper objectMapper,
            @Value("${pubsub.topic.user-registration}") String userRegistrationTopic,
            @Value("${pubsub.topic.account-approved}") String accountApprovedTopic) {
        this.pubSubTemplate = pubSubTemplate;
        this.objectMapper = objectMapper;
        this.userRegistrationTopic = userRegistrationTopic;
        this.accountApprovedTopic = accountApprovedTopic;
    }

    @PostConstruct
    public void validateTopicNames() {
        if (!Constants.TOPIC_USER_REGISTRATION.equals(userRegistrationTopic)) {
            throw new IllegalStateException(
                String.format("Configured user registration topic name '%s' does not match expected value '%s'",
                    userRegistrationTopic, Constants.TOPIC_USER_REGISTRATION)
            );
        }
        if (!Constants.TOPIC_ACCOUNT_APPROVED.equals(accountApprovedTopic)) {
            throw new IllegalStateException(
                String.format("Configured account approved topic name '%s' does not match expected value '%s'",
                    accountApprovedTopic, Constants.TOPIC_ACCOUNT_APPROVED)
            );
        }
        logger.info("Validated PubSub topic names - User Registration: {}, Account Approved: {}", 
            userRegistrationTopic, accountApprovedTopic);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> publishUserRegistrationEvent(UserRegistrationData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Attempting to publish event for user: {} to topic: {}", data.getUsername(), userRegistrationTopic);
                PubSubEvent<UserRegistrationData> event = new PubSubEvent<>();
                event.setEventType(Constants.EVENT_USER_REGISTERED);
                event.setTimestamp(Instant.now().toString());
                event.setData(data);

                String message = objectMapper.writeValueAsString(event);
                logger.info("Publishing message: {}", message);
                
                pubSubTemplate.publish(userRegistrationTopic, message);
                logger.info("Successfully published user registration event for user: {} with RMG email: {}", 
                    data.getUsername(), data.getRmgEmail());
                System.out.println("Successfully published user registration event for user"+message);
            } catch (Exception e) {
                logger.error("Failed to publish user registration event for user: {}. Error: {}", 
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
    public CompletableFuture<Void> publishAccountApprovedEvent(AccountApprovalData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Attempting to publish account approved event for user: {} to topic: {}", 
                    data.getUsername(), accountApprovedTopic);

                // Set approval status in the data object
                data.setApproval(true);

                PubSubEvent<AccountApprovalData> event = new PubSubEvent<>(
                    Constants.EVENT_ACCOUNT_APPROVED,
                    Instant.now().toString(),
                    data
                );

                String message = objectMapper.writeValueAsString(event);
                logger.info("Publishing message: {}", message);
                
                pubSubTemplate.publish(accountApprovedTopic, message);
                logger.info("Successfully published account approved event for user: {} approved by: {}", 
                    data.getUsername(), data.getApprovedBy());
            } catch (Exception e) {
                logger.error("Failed to publish account approved event for user: {}. Error: {}", 
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
