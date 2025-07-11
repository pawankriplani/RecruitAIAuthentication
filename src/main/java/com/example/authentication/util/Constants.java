package com.example.authentication.util;

public class Constants {
    // User Roles
    public static final String ROLE_MANAGER = "Manager";
    public static final String ROLE_RMG = "RMG";

    // Account Status
    public static final String STATUS_PENDING = "PENDING";

    // Event Types
    public static final String EVENT_USER_REGISTERED = "UserRegistered";
    public static final String EVENT_ACCOUNT_APPROVED = "AccountApproved";
    public static final String EVENT_ACCOUNT_REJECTED = "AccountRejected";

    // Error Messages
    public static final String ERROR_USERNAME_TAKEN = "Error: Username is already taken!";
    public static final String ERROR_EMAIL_IN_USE = "Error: Email is already in use!";
    public static final String ERROR_ROLE_NOT_FOUND = "Error: Role is not found.";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";
    public static final String INVALID_PASSWORD_MESSAGE = "Invalid password. %d attempts remaining before account lockout.";
    public static final String ACCOUNT_LOCKED_MESSAGE = "Account has been locked due to too many failed attempts. Please contact administrator.";

    // Authentication Settings
    public static final int MAX_FAILED_ATTEMPTS = 5;

    // Success Messages
    public static final String SUCCESS_USER_REGISTERED = "User registered successfully!";

    // Notification Event Names
    public static final String NOTIFICATION_USER_REGISTRATION = "UserRegisterNotification";
    public static final String NOTIFICATION_ACCOUNT_APPROVED = "AccountApprovedNotification";

    // Notification URL
    public static final String NOTIFICATION_URL = "http://localhost:1000/api/notifications";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
