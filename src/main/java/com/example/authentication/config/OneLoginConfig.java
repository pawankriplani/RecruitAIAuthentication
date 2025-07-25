package com.example.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OneLoginConfig {

    @Value("${onelogin.client-id}")
    private String clientId;

    @Value("${onelogin.client-secret}")
    private String clientSecret;

    @Value("${onelogin.token-url}")
    private String tokenUrl;

    @Value("${onelogin.redirect-uri}")
    private String redirectUri;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
