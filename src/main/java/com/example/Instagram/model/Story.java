package com.example.Instagram.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "stories")
public class Story {

    @Id
    private String id;

    private String userId;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}