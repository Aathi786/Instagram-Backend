package com.example.Instagram.controller;

import com.example.Instagram.model.Post;
import com.example.Instagram.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(
            @PathVariable String id,
            @RequestBody Post post,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        return postService.updatePost(
                id,
                post,
                currentUserId
        );
    }

    @DeleteMapping("/{id}")
    public String deletePost(
            @PathVariable String id,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        postService.deletePost(
                id,
                currentUserId
        );

        return "Post deleted successfully";
    }


}