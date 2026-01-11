package com.example.authentication.service;

import com.example.authentication.response.ApiResponse;
import com.example.authentication.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    
    public <T> ApiResponse<T> successResponse(T data) {
        return ApiResponse.success(data);
    }
    
    public <T> ApiResponse<T> errorResponse(String message, HttpStatus status) {
        return ApiResponse.error(status.value(), message);
    }
}
