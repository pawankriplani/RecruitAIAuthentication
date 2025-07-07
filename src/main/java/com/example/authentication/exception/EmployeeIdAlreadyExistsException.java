package com.example.authentication.exception;

public class EmployeeIdAlreadyExistsException extends RuntimeException {
    public EmployeeIdAlreadyExistsException(String message) {
        super(message);
    }
}
