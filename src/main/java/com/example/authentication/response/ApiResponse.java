package com.example.authentication.response;

import java.util.List;

/**
 * Generic API response wrapper for standardizing API responses across the application.
 * @param <T> The type of data contained in the response
 */
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int statusCode;
    private List<String> errors;
    private long timestamp;

    // Private constructor to enforce the use of static factory methods
    private ApiResponse(boolean success, T data, String message, int statusCode, List<String> errors, long timestamp) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Success", 200, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, 200, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, null, message, statusCode, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message, int statusCode, List<String> errors) {
        return new ApiResponse<>(false, null, message, statusCode, errors, System.currentTimeMillis());
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
