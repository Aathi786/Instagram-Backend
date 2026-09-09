package com.example.Instagram.controller;

import com.example.Instagram.model.Comment;
import com.example.Instagram.model.CommentResponse;
import com.example.Instagram.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    public Comment createComment(
            @RequestBody Comment comment,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        comment.setUserId(currentUserId);

        return commentService.createComment(comment);
    }



    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }
    @GetMapping("/post/{postId}")
    public List<CommentResponse> getCommentsByPostId(@PathVariable String postId) {
        return commentService.getCommentsByPostId(postId);
    }
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable String id) {
        return commentService.getCommentById(id);
    }
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id, @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }
    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return "Comment deleted successfully";
    }
}