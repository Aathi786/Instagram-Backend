package com.example.Instagram.model;

import lombok.Data;

@Data
public class ProfileUpdateRequest {

    private String username;
    private String profilePic;
    private String bio;
}