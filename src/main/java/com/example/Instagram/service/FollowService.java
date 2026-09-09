
        package com.example.Instagram.service;

import com.example.Instagram.model.Follow;
import com.example.Instagram.model.FollowResponse;
import com.example.Instagram.model.Notification;
import com.example.Instagram.model.User;
import com.example.Instagram.repository.FollowRepository;
import com.example.Instagram.repository.NotificationRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

        @Service
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
            private final NotificationRepository notificationRepository;

    public FollowService(
            UserRepository userRepository,
            FollowRepository followRepository, NotificationRepository notificationRepository) {

        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.notificationRepository = notificationRepository;
    }


    // =========================
    // FOLLOW
    // =========================

            public Follow followUser(Follow follow) {

                if (follow.getFollowerId().equals(follow.getFollowingId())) {
                    throw new RuntimeException("You cannot follow yourself");
                }

                Optional<Follow> existingFollow =
                        followRepository.findByFollowerIdAndFollowingId(
                                follow.getFollowerId(),
                                follow.getFollowingId()
                        );

                if (existingFollow.isPresent()) {
                    throw new RuntimeException("Already following");
                }

                follow.setCreatedAt(java.time.LocalDateTime.now());

                Follow savedFollow = followRepository.save(follow);

                // Create notification
                Notification notification = new Notification();

                notification.setUserId(follow.getFollowingId());
                notification.setSenderId(follow.getFollowerId());
                notification.setType("FOLLOW");
                notification.setMessage("Someone started following you");
                notification.setPostId(null);
                notification.setRead(false);
                notification.setCreatedAt(LocalDateTime.now());

                System.out.println("========== NOTIFICATION DEBUG ==========");
                System.out.println("Notification User ID   : " + notification.getUserId());
                System.out.println("Notification Sender ID : " + notification.getSenderId());
                System.out.println("Notification Type      : " + notification.getType());
                System.out.println("Notification Message   : " + notification.getMessage());

                Notification savedNotification =
                        notificationRepository.save(notification);

                System.out.println("Notification SAVED!");
                System.out.println("Notification ID        : " + savedNotification.getId());
                System.out.println("========================================");

                notificationRepository.save(notification);

                return savedFollow;
            }


    // =========================
    // GET FOLLOWING
    // =========================

    public List<FollowResponse> getFollowing(String followerId) {

        List<Follow> follows =
                followRepository.findByFollowerId(followerId);

        List<FollowResponse> responses = new ArrayList<>();

        for (Follow follow : follows) {

            User user = userRepository
                    .findById(follow.getFollowingId())
                    .orElse(null);

            if (user == null) {
                continue;
            }

            FollowResponse response = new FollowResponse();

            response.setId(follow.getId());
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setProfilePic(user.getProfilePic());

            responses.add(response);
        }

        return responses;
    }


    // =========================
    // GET FOLLOWERS
    // =========================

    public List<FollowResponse> getFollowers(String followingId) {

        List<Follow> follows =
                followRepository.findByFollowingId(followingId);

        List<FollowResponse> responses = new ArrayList<>();

        for (Follow follow : follows) {

            User user = userRepository
                    .findById(follow.getFollowerId())
                    .orElse(null);

            if (user == null) {
                continue;
            }

            FollowResponse response = new FollowResponse();

            response.setId(follow.getId());
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setProfilePic(user.getProfilePic());

            responses.add(response);
        }

        return responses;
    }


    // =========================
    // UNFOLLOW
    // =========================

    public void unfollowUser(String followerId, String followingId) {

        System.out.println("========== UNFOLLOW DEBUG ==========");
        System.out.println("Follower ID  : " + followerId);
        System.out.println("Following ID : " + followingId);

        if (followerId == null || followerId.isBlank()) {
            throw new RuntimeException("Follower ID is missing");
        }

        if (followingId == null || followingId.isBlank()
                || followingId.equals("undefined")) {
            throw new RuntimeException("Following ID is missing");
        }

        Optional<Follow> follow =
                followRepository.findByFollowerIdAndFollowingId(
                        followerId,
                        followingId
                );

        System.out.println("Follow found : " + follow.isPresent());

        if (follow.isEmpty()) {
            throw new RuntimeException(
                    "Follow relationship not found"
            );
        }

        followRepository.delete(follow.get());

        System.out.println("Unfollow successful");
        System.out.println("====================================");
    }

    // =========================
    // FOLLOWING COUNT
    // =========================

    public long getFollowingCount(String userId) {

        return followRepository.countByFollowerId(userId);
    }


    // =========================
    // FOLLOWERS COUNT
    // =========================

    public long getFollowersCount(String userId) {

        return followRepository.countByFollowingId(userId);
    }
}

