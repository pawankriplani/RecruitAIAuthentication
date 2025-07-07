package com.example.authentication.exception;

public class RejectedAccountException extends RuntimeException {
    public RejectedAccountException(String message) {
        super(message);
    }
}
