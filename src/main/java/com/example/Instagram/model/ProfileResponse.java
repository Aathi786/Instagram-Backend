package com.example.Instagram.model;

import lombok.Data;

@Data
public class ProfileResponse {

    private String id;
    private String username;
    private String profilePic;
    private String bio;

    private long followersCount;
    private long followingCount;
    private long postsCount;
}