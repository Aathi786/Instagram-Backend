package com.example.Instagram.service;

import com.example.Instagram.model.Comment;
import com.example.Instagram.model.CommentResponse;
import com.example.Instagram.model.User;
import com.example.Instagram.repository.CommentRepository;
import com.example.Instagram.repository.NotificationRepository;
import com.example.Instagram.repository.PostRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.Instagram.model.Notification;
import com.example.Instagram.model.Post;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, NotificationRepository notificationRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {

        comment.setCreatedAt(
                java.time.LocalDateTime.now()
        );

        Comment savedComment =
                commentRepository.save(comment);

        // Find post owner
        Post post = postRepository
                .findById(comment.getPostId())
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // Don't notify yourself
        if (!post.getUserId().equals(comment.getUserId())) {

            Notification notification = new Notification();

            notification.setUserId(post.getUserId());
            notification.setSenderId(comment.getUserId());
            notification.setType("COMMENT");
            notification.setMessage("Someone commented on your post");
            notification.setPostId(comment.getPostId());
            notification.setRead(false);
            notification.setCreatedAt(
                    java.time.LocalDateTime.now()
            );

            notificationRepository.save(notification);
        }

        return savedComment;
    }
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    public List<CommentResponse> getCommentsByPostId(String postId) {

        List<Comment> comments =
                commentRepository.findByPostId(postId);

        List<CommentResponse> responses = new ArrayList<>();

        for (Comment comment : comments) {

            CommentResponse response = new CommentResponse();

            response.setId(comment.getId());
            response.setPostId(comment.getPostId());
            response.setUserId(comment.getUserId());
            response.setText(comment.getText());
            response.setCreatedAt(comment.getCreatedAt());

            User user = userRepository
                    .findById(comment.getUserId())
                    .orElse(null);

            if (user != null) {
                response.setUsername(user.getUsername());
            }

            responses.add(response);
        }

        return responses;
    }
    public Comment getCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
    public Comment updateComment(String id, Comment comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }
    public void deleteComment(String id) {

        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }

        commentRepository.deleteById(id);
    }

}