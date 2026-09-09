package com.example.Instagram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private String id;
    private String postId;
    private String userId;
    private String username;
    private String text;
    private LocalDateTime createdAt;
}