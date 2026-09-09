package com.example.Instagram.controller;

import com.example.Instagram.model.Follow;
import com.example.Instagram.model.FollowResponse;
import com.example.Instagram.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "http://localhost:5173")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public Follow followUser(
            @RequestBody Follow follow,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        follow.setFollowerId(currentUserId);

        return followService.followUser(follow);
    }

    @GetMapping("/following/{userId}")
    public List<FollowResponse> getFollowing(@PathVariable String userId) {
        return followService.getFollowing(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<FollowResponse> getFollowers(@PathVariable String userId) {
        return followService.getFollowers(userId);
    }
    @DeleteMapping
    public String unfollowUser(
            @RequestParam String followerId,
            @RequestParam String followingId) {

        followService.unfollowUser(followerId, followingId);

        return "Unfollowed successfully";
    }
    @GetMapping("/following/count/{userId}")
    public long getFollowingCount(@PathVariable String userId) {
        return followService.getFollowingCount(userId);
    }

    @GetMapping("/followers/count/{userId}")
    public long getFollowersCount(@PathVariable String userId) {
        return followService.getFollowersCount(userId);
    }
}