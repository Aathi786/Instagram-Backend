package com.example.Instagram.repository;

import com.example.Instagram.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    long countByUserId(String userId);
    List<Post> findByUserIdIn(List<String> userIds);
}