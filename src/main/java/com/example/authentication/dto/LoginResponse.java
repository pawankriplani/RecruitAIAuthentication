package com.example.authentication.dto;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private String idToken;
    private UserDto user;

    public LoginResponse() {
    }

    public LoginResponse(String token, String refreshToken, UserDto user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public LoginResponse(String token, String refreshToken, String idToken, UserDto user) {
		super();
		this.token = token;
		this.refreshToken = refreshToken;
		this.idToken = idToken;
		this.user = user;
	}

	// Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
}
