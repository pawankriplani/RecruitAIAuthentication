package com.example.authentication.service;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.UnlockAccountRequest;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refreshToken(String refreshToken);
    void unlockAccount(UnlockAccountRequest unlockAccountRequest);
}
