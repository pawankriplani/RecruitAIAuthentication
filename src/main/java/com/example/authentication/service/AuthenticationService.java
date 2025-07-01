package com.example.authentication.service;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refreshToken(String refreshToken);
}
