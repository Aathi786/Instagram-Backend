package com.example.Instagram.model;

import lombok.Data;

@Data
public class FollowResponse {

    private String id;
    private String userId;
    private String username;
    private String profilePic;
}