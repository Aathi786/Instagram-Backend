package com.example.Instagram.service;

import com.example.Instagram.model.Story;
import com.example.Instagram.model.StoryResponse;
import com.example.Instagram.model.User;
import com.example.Instagram.repository.StoryRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    public StoryService(
            StoryRepository storyRepository,
            UserRepository userRepository) {

        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
    }


    // =========================
    // CREATE STORY
    // =========================

    public Story createStory(Story story) {

        LocalDateTime now = LocalDateTime.now();

        story.setCreatedAt(now);
        story.setExpiresAt(
                now.plusHours(24)
        );

        return storyRepository.save(story);
    }


    // =========================
    // GET ACTIVE STORIES
    // =========================

    public List<StoryResponse> getActiveStories() {

        LocalDateTime now =
                LocalDateTime.now();

        List<Story> stories =
                storyRepository
                        .findByExpiresAtAfter(now);

        List<StoryResponse> responses =
                new ArrayList<>();

        for (Story story : stories) {

            StoryResponse response =
                    new StoryResponse();

            response.setId(
                    story.getId()
            );

            response.setUserId(
                    story.getUserId()
            );

            response.setImageUrl(
                    story.getImageUrl()
            );

            response.setCreatedAt(
                    story.getCreatedAt()
            );

            response.setExpiresAt(
                    story.getExpiresAt()
            );


            User user = userRepository
                    .findById(story.getUserId())
                    .orElse(null);

            if (user != null) {

                response.setUsername(
                        user.getUsername()
                );

                response.setProfilePic(
                        user.getProfilePic()
                );
            }

            responses.add(response);
        }

        return responses;
    }


    // =========================
    // GET USER ACTIVE STORIES
    // =========================

    public List<StoryResponse> getUserActiveStories(
            String userId) {

        List<Story> stories =
                storyRepository
                        .findByUserIdAndExpiresAtAfter(
                                userId,
                                LocalDateTime.now()
                        );

        return convertToResponse(stories);
    }


    // =========================
    // CONVERT TO RESPONSE
    // =========================

    private List<StoryResponse> convertToResponse(
            List<Story> stories) {

        List<StoryResponse> responses =
                new ArrayList<>();

        for (Story story : stories) {

            User user = userRepository
                    .findById(story.getUserId())
                    .orElse(null);

            if (user == null) {
                continue;
            }

            StoryResponse response =
                    new StoryResponse();

            response.setId(
                    story.getId()
            );

            response.setUserId(
                    user.getId()
            );

            response.setUsername(
                    user.getUsername()
            );

            response.setProfilePic(
                    user.getProfilePic()
            );

            response.setImageUrl(
                    story.getImageUrl()
            );

            response.setCreatedAt(
                    story.getCreatedAt()
            );

            response.setExpiresAt(
                    story.getExpiresAt()
            );

            responses.add(response);
        }

        return responses;
    }


    // =========================
    // DELETE STORY
    // =========================

    public void deleteStory(
            String id,
            String currentUserId) {

        Story story =
                storyRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Story not found"
                                )
                        );


        // Ownership check
        if (!story
                .getUserId()
                .equals(currentUserId)) {

            throw new RuntimeException(
                    "You can only delete your own story"
            );
        }


        storyRepository.deleteById(id);
    }


    // =========================
    // DELETE EXPIRED STORIES
    // =========================

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredStories() {

        LocalDateTime now =
                LocalDateTime.now();

        List<Story> expiredStories =
                storyRepository
                        .findByExpiresAtBefore(now);

        storyRepository.deleteAll(
                expiredStories
        );
    }
}