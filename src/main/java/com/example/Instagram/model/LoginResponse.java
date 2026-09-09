package com.example.Instagram.model;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String userId;
    private String username;

    public LoginResponse(
            String token,
            String userId,
            String username
    ) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }
}