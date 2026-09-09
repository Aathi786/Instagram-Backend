package com.example.Instagram.service;

import com.example.Instagram.model.FeedResponse;
import com.example.Instagram.model.Follow;
import com.example.Instagram.model.Post;
import com.example.Instagram.model.User;
import com.example.Instagram.repository.CommentRepository;
import com.example.Instagram.repository.FollowRepository;
import com.example.Instagram.repository.LikeRepository;
import com.example.Instagram.repository.PostRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public FeedService(
            PostRepository postRepository,
            FollowRepository followRepository,
            UserRepository userRepository,
            LikeRepository likeRepository,
            CommentRepository commentRepository) {

        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public List<FeedResponse> getFeed(String userId) {

        // 1. Get users whom current user follows
        List<Follow> following =
                followRepository.findByFollowerId(userId);

        // 2. Collect their user IDs
        List<String> userIds = new ArrayList<>();

        for (Follow follow : following) {
            userIds.add(follow.getFollowingId());
        }

        // Include current user's own posts
        userIds.add(userId);

        // 3. Get all posts from these users
        List<Post> posts =
                postRepository.findByUserIdIn(userIds);

        // 4. Newest posts first
        posts.sort(
                Comparator.comparing(
                        Post::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        // 5. Convert Post → FeedResponse
        List<FeedResponse> feed = new ArrayList<>();

        for (Post post : posts) {

            User user = userRepository
                    .findById(post.getUserId())
                    .orElse(null);

            if (user == null) {
                continue;
            }

            FeedResponse response = new FeedResponse();

            response.setPostId(post.getId());
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setProfilePic(user.getProfilePic());

            response.setImageUrl(post.getImageUrl());
            response.setCaption(post.getCaption());
            response.setCreatedAt(post.getCreatedAt());

            response.setLikesCount(
                    likeRepository.countByPostId(post.getId())
            );

            response.setCommentsCount(
                    commentRepository.countByPostId(post.getId())
            );
            response.setLikedByCurrentUser(
                    likeRepository
                            .findByPostIdAndUserId(
                                    post.getId(),
                                    userId
                            )
                            .isPresent()
            );

            feed.add(response);
        }

        return feed;
    }
}