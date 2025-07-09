package com.example.authentication.controller;

import com.example.authentication.dto.*;
import com.example.authentication.response.ApiResponse;
import com.example.authentication.service.AuthenticationService;
import com.example.authentication.service.ResponseService;
import com.example.authentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ResponseService responseService;

    @Autowired
    public AuthController(UserService userService, AuthenticationService authenticationService, ResponseService responseService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.responseService = responseService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = userService.registerUser(request);
        return ResponseEntity.ok(responseService.successResponse(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(responseService.successResponse(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestBody String refreshToken) {
        LoginResponse loginResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(responseService.successResponse(loginResponse));
    }

    @PostMapping("/unlock")
    public ResponseEntity<ApiResponse<String>> unlockAccount(@Valid @RequestBody UnlockAccountRequest unlockAccountRequest) {
        authenticationService.unlockAccount(unlockAccountRequest);
        return ResponseEntity.ok(responseService.successResponse("Account unlocked successfully"));
    }
}
