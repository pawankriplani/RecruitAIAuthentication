package com.example.authentication.controller;

import com.example.authentication.service.OneLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> exchangeCode(@RequestParam(required = false) String code,
                                          @RequestBody(required = false) Map<String, String> body) {
        logger.info("Received code exchange request " + code + " " + body);
        
        String authCode = code;
        if (authCode == null && body != null) {
            authCode = body.get("code");
        }
        
        if (authCode == null || authCode.trim().isEmpty()) {
            logger.error("Authorization code is missing or empty");
            return ResponseEntity.badRequest().body("Authorization code is required");
        }

        return oneLoginService.exchangeCodeForToken(authCode);
    }
}
