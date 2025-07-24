package com.example.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authentication.config.OneLoginConfig;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.UserDto;
import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.response.ApiResponse;
import com.example.authentication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OneLoginService {
    private static final Logger logger = LoggerFactory.getLogger(OneLoginService.class);

    @Autowired
    private OneLoginConfig oneLoginConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<ApiResponse<LoginResponse>> exchangeCodeForToken(String code) {
        try {
            logger.info("Exchanging authorization code for token");
            
            HttpHeaders headers = createAuthHeaders();
            MultiValueMap<String, String> body = createTokenRequestBody(code);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                oneLoginConfig.getTokenUrl(),
                request,
                Map.class
            );

            if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Token exchange failed with status: {}", tokenResponse.getStatusCode());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Failed to get token"));
            }

            return processTokenResponse(tokenResponse.getBody());

        } catch (Exception e) {
            logger.error("Error during token exchange", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    "Error processing token exchange: " + e.getMessage()));
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String credentials = oneLoginConfig.getClientId() + ":" + oneLoginConfig.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.set("Authorization", "Basic " + encodedCredentials);
        return headers;
    }

    private MultiValueMap<String, String> createTokenRequestBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", oneLoginConfig.getRedirectUri());
        return body;
    }

    @Transactional
    private ResponseEntity<ApiResponse<LoginResponse>> processTokenResponse(Map<String, Object> tokenData) {
        if (tokenData == null) {
            logger.error("Token response body is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid token response"));
        }

        String idToken = (String) tokenData.get("id_token");
        if (idToken == null) {
            logger.error("ID token not found in response");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "ID token not found"));
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(idToken);
            String email = decodedJWT.getClaim("email").asString();
            
            if (email == null) {
                logger.error("Email not found in ID token");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Email not found in token"));
            }

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDto userDto = userService.convertToDto(user);
                LoginResponse loginResponse = new LoginResponse(
                    (String) tokenData.get("access_token"),
                    (String) tokenData.get("refresh_token"),
                    userDto
                );
                
                return ResponseEntity.ok(ApiResponse.success(loginResponse));
            } else {
                logger.warn("User not found for email: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "User not found. Please register."));
            }
        } catch (JWTDecodeException e) {
            logger.error("Error decoding JWT", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid ID token"));
        }
    }
}
