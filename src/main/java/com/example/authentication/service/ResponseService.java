package com.example.authentication.service;

import com.example.authentication.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    
    public <T> ApiResponse<T> successResponse(T data) {
        return ApiResponse.success(data);
    }
    
    public <T> ApiResponse<T> successResponse(T data, String message) {
        return ApiResponse.success(data, message);
    }
    
    public <T> ApiResponse<T> errorResponse(String message, HttpStatus status) {
        return ApiResponse.error(message, status.value());
    }
    
    public <T> ApiResponse<T> errorResponse(String message, HttpStatus status, List<String> errors) {
        return ApiResponse.error(message, status.value(), errors);
    }
}
