package com.example.Instagram.controller;

import com.example.Instagram.model.Like;
import com.example.Instagram.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "http://localhost:5173")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public Like createLike(
            @RequestBody Like like,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        like.setUserId(currentUserId);

        return likeService.createLike(like);
    }

    @GetMapping
    public List<Like> getAllLikes() {
        return likeService.getAllLikes();
    }
    @GetMapping("/post/{postId}")
    public List<Like> getLikesByPostId(@PathVariable String postId) {
        return likeService.getLikesByPostId(postId);
    }
    @DeleteMapping
    public String unlikePost(
            @RequestParam String postId,
            @RequestParam String userId) {

        likeService.deleteLike(postId, userId);

        return "Post unliked successfully";
    }
    @GetMapping("/count/{postId}")
    public long getLikeCount(@PathVariable String postId) {
        return likeService.getLikeCount(postId);
    }
}