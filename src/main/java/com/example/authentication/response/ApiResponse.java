package com.example.authentication.response;

/**
 * Generic API response wrapper for standardizing API responses across the application.
 * @param <T> The type of data contained in the response
 */
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private int statusCode;
    private Error errors;
    private long timestamp;

    // Private constructor to enforce the use of static factory methods
    private ApiResponse(boolean success, T data, int statusCode, Error errors, long timestamp) {
        this.success = success;
        this.data = data;
        this.statusCode = statusCode;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    // Static factory methods for common responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, 200, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(int statusCode, String errorMessage) {
        return new ApiResponse<>(false, null, statusCode, new Error(errorMessage), System.currentTimeMillis());
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
