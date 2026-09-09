package com.example.Instagram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoryResponse {

    private String id;

    private String userId;

    private String username;

    private String profilePic;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}