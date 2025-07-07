package com.example.authentication.exception;

import com.example.authentication.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred. Please try again later."
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        logger.warn("Username already taken", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.CONFLICT.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        logger.warn("Email already in use", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.CONFLICT.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmployeeIdAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmployeeIdAlreadyExistsException(EmployeeIdAlreadyExistsException ex) {
        logger.warn("Employee ID already exists", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.CONFLICT.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.warn("Validation error", ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Validation error: " + errorMessage
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        logger.warn("Constraint violation", ex);
        String errorMessage = ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Validation error: " + errorMessage
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Illegal argument", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid input: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(IllegalStateException ex) {
        logger.error("Illegal state", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "The system is in an invalid state to process your request. Please try again later."
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation", ex);
        String message = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        String errorMessage;

        if (message.contains("employee_id")) {
            errorMessage = "An employee with this Employee ID already exists. Please use a unique Employee ID.";
        } else if (message.contains("email")) {
            errorMessage = "An account with this email address already exists. Please use a different email.";
        } else if (message.contains("username")) {
            errorMessage = "This username is already taken. Please choose a different username.";
        } else {
            errorMessage = "A data integrity constraint was violated. Please check your input and try again.";
        }

        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.CONFLICT.value(),
            errorMessage
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("Access denied", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.FORBIDDEN.value(),
            "Access denied. You do not have permission to perform this action."
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PendingAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handlePendingAccount(PendingAccountException ex) {
        logger.warn("Pending account access attempt", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.FORBIDDEN.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handleInactiveAccount(InactiveAccountException ex) {
        logger.warn("Inactive account access attempt", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.FORBIDDEN.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RejectedAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handleRejectedAccount(RejectedAccountException ex) {
        logger.warn("Rejected account access attempt", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.FORBIDDEN.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailNotFound(EmailNotFoundException ex) {
        logger.warn("Login attempt with non-existent email", ex);
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
