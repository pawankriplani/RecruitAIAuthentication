package com.example.authentication.controller;

import com.example.authentication.service.OneLoginService;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthOneLoginController {
    private static final Logger logger = LoggerFactory.getLogger(AuthOneLoginController.class);

    @Autowired
    private OneLoginService oneLoginService;

    @PostMapping(value = "/exchange", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse<LoginResponse>> exchangeCode(@RequestParam(required = false) String code,
                                          @RequestBody(required = false) Map<String, String> body) {
        logger.info("Received code exchange request - Query param code: {}, Request body: {}", code, body);
        
        String authCode = code;
        if (authCode == null && body != null) {
            authCode = body.get("code");
        }
        
        if (authCode == null || authCode.trim().isEmpty()) {
            logger.error("Authorization code is missing or empty");
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Authorization code is required"));
        }

        logger.info("Using authorization code: {}", authCode);
        ResponseEntity<ApiResponse<LoginResponse>> response = oneLoginService.exchangeCodeForToken(authCode);
        logger.info("Response status: {}", response.getStatusCode());
        return response;
    }
}
