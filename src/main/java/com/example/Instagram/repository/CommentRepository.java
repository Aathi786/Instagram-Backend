package com.example.Instagram.repository;

import com.example.Instagram.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId);
    long countByPostId(String postId);
}