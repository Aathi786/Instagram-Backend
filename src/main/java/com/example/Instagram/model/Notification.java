package com.example.Instagram.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String userId;
    private String senderId;
    private String type;
    private String message;
    private String postId;
    private boolean read;
    private LocalDateTime createdAt;
}