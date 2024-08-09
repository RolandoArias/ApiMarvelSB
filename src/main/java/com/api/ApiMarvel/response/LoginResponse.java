package com.api.ApiMarvel.response;

public class LoginResponse {
    private String token;
    private long expiresIn;

    // Getter and Setter for token with method chaining
    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    // Getter and Setter for expiresIn with method chaining
    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
