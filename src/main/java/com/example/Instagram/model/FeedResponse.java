package com.example.Instagram.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedResponse {

    private String postId;

    // Post owner's information
    private String userId;
    private String username;
    private String profilePic;

    // Post information
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;

    // Engagement information
    private long likesCount;
    private long commentsCount;
    private boolean likedByCurrentUser;

}