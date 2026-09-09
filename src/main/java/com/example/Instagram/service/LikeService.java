package com.example.Instagram.service;

import com.example.Instagram.model.Like;
import com.example.Instagram.model.Notification;
import com.example.Instagram.model.Post;
import com.example.Instagram.repository.LikeRepository;
import com.example.Instagram.repository.NotificationRepository;
import com.example.Instagram.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, NotificationRepository notificationRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
    }

    public Like createLike(Like like) {

        Optional<Like> existingLike =
                likeRepository.findByPostIdAndUserId(
                        like.getPostId(),
                        like.getUserId()
                );

        if (existingLike.isPresent()) {
            throw new RuntimeException("Already liked");
        }

        like.setCreatedAt(java.time.LocalDateTime.now());

        Like savedLike = likeRepository.save(like);

        // Find post owner
        Post post = postRepository
                .findById(like.getPostId())
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // Create notification
        // Don't notify yourself
        if (!post.getUserId().equals(like.getUserId())) {

            Notification notification = new Notification();

            notification.setUserId(post.getUserId());
            notification.setSenderId(like.getUserId());
            notification.setType("LIKE");
            notification.setMessage("Someone liked your post");
            notification.setPostId(like.getPostId());
            notification.setRead(false);
            notification.setCreatedAt(
                    java.time.LocalDateTime.now()
            );

            notificationRepository.save(notification);
        }

        return savedLike;
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }
    public List<Like> getLikesByPostId(String postId) {
        return likeRepository.findByPostId(postId);
    }
    public void deleteLike(String postId, String userId) {

        Like like = likeRepository
                .findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
    }
    public long getLikeCount(String postId) {
        return likeRepository.countByPostId(postId);
    }
}