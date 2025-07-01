package com.example.authentication.service.impl;

import com.example.authentication.event.PubSubEvent;
import com.example.authentication.event.UserRegistrationData;
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
    private final String topicName;

    @Autowired
    public PubSubServiceImpl(
            PubSubTemplate pubSubTemplate,
            ObjectMapper objectMapper,
            @Value("${pubsub.topic.user-registration}") String topicName) {
        this.pubSubTemplate = pubSubTemplate;
        this.objectMapper = objectMapper;
        this.topicName = topicName;
    }

    @PostConstruct
    public void validateTopicName() {
        if (!Constants.TOPIC_USER_REGISTRATION.equals(topicName)) {
            throw new IllegalStateException(
                String.format("Configured topic name '%s' does not match expected value '%s'",
                    topicName, Constants.TOPIC_USER_REGISTRATION)
            );
        }
        logger.info("Validated PubSub topic name: {}", topicName);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> publishUserRegistrationEvent(UserRegistrationData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                logger.info("Attempting to publish event for user: {} to topic: {}", data.getUsername(), topicName);
                PubSubEvent<UserRegistrationData> event = new PubSubEvent<>();
                event.setTimestamp(Instant.now().toString());
                event.setData(data);

                String message = objectMapper.writeValueAsString(event);
                logger.info("Publishing message: {}", message);
                
                pubSubTemplate.publish(topicName, message);
                logger.info("Successfully published user registration event for user: {} with RMG email: {}", 
                    data.getUsername(), data.getRmgEmail());
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
}
