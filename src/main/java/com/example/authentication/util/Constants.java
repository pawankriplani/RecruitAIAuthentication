package com.example.authentication.util;

public class Constants {
    // User Roles
    public static final String ROLE_MANAGER = "Manager";
    public static final String ROLE_RMG = "RMG";

    // Account Status
    public static final String STATUS_PENDING = "pending";

    // Event Types
    public static final String EVENT_USER_REGISTERED = "UserRegistered";

    // Error Messages
    public static final String ERROR_USERNAME_TAKEN = "Error: Username is already taken!";
    public static final String ERROR_EMAIL_IN_USE = "Error: Email is already in use!";
    public static final String ERROR_ROLE_NOT_FOUND = "Error: Role is not found.";

    // Success Messages
    public static final String SUCCESS_USER_REGISTERED = "User registered successfully!";

    // PubSub Topic Names
    public static final String TOPIC_USER_REGISTRATION = "UserRegisterEvent";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
