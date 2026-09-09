package com.example.Instagram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private String id;
    private String userId;
    private String senderId;
    private String senderUsername;
    private String senderProfilePic;
    private String type;
    private String message;
    private String postId;
    private boolean read;
    private LocalDateTime createdAt;
}