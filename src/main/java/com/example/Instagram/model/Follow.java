package com.example.Instagram.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "follows")
public class Follow {

    @Id
    private String id;

    private String followerId;
    private String followingId;
    private LocalDateTime createdAt;
}