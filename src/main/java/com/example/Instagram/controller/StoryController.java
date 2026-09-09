package com.example.Instagram.controller;

import com.example.Instagram.model.Story;
import com.example.Instagram.model.StoryResponse;
import com.example.Instagram.service.StoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@CrossOrigin(origins = "http://localhost:5173")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }


    // =========================
    // CREATE STORY
    // =========================

    @PostMapping
    public Story createStory(
            @RequestBody Story story,
            Authentication authentication
    ) {

        // Get logged-in user ID from JWT
        String currentUserId =
                authentication.getName();

        // Never trust userId from frontend
        story.setUserId(currentUserId);

        return storyService.createStory(story);
    }


    // =========================
    // GET ACTIVE STORIES
    // =========================

    @GetMapping
    public List<StoryResponse> getActiveStories() {

        return storyService.getActiveStories();
    }


    // =========================
    // GET USER STORIES
    // =========================

    @GetMapping("/user/{userId}")
    public List<StoryResponse> getUserActiveStories(
            @PathVariable String userId
    ) {

        return storyService
                .getUserActiveStories(userId);
    }


    // =========================
    // DELETE STORY
    // =========================

    @DeleteMapping("/{id}")
    public String deleteStory(
            @PathVariable String id,
            Authentication authentication
    ) {

        String currentUserId =
                authentication.getName();

        storyService.deleteStory(
                id,
                currentUserId
        );

        return "Story deleted successfully";
    }
}