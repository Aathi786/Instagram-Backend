package com.example.Instagram.controller;

import com.example.Instagram.model.FeedResponse;
import com.example.Instagram.service.FeedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{userId}")
    public List<FeedResponse> getFeed(@PathVariable String userId) {
        return feedService.getFeed(userId);
    }
}