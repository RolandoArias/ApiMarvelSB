package com.api.ApiMarvel.dtos;

import lombok.Getter;

@Getter
public class LoginUserDto {
    // Getter and Setter for email with method chaining
    private String email;
    // Getter and Setter for password with method chaining
    private String password;

    public LoginUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginUserDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
