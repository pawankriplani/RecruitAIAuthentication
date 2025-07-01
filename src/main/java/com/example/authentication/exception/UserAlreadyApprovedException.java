package com.example.authentication.exception;

public class UserAlreadyApprovedException extends RuntimeException {
    public UserAlreadyApprovedException(String message) {
        super(message);
    }
}
