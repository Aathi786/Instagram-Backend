package com.example.Instagram.service;

import com.example.Instagram.model.Post;
import com.example.Instagram.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post updatePost(
            String id,
            Post post,
            String currentUserId
    ) {

        Post existingPost = postRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post not found")
                );

        // Only the post owner can update
        if (!existingPost.getUserId().equals(currentUserId)) {
            throw new RuntimeException(
                    "You can only update your own post"
            );
        }

        existingPost.setImageUrl(post.getImageUrl());
        existingPost.setCaption(post.getCaption());

        return postRepository.save(existingPost);
    }


    public void deletePost(
            String id,
            String currentUserId
    ) {

        Post existingPost = postRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post not found")
                );

        // Only the post owner can delete
        if (!existingPost.getUserId().equals(currentUserId)) {
            throw new RuntimeException(
                    "You can only delete your own post"
            );
        }

        postRepository.deleteById(id);
    }


}