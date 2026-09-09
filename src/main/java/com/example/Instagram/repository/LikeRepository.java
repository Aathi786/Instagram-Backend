package com.example.Instagram.repository;

import com.example.Instagram.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String> {

    List<Like> findByPostId(String postId);

    Optional<Like> findByPostIdAndUserId(String postId, String userId);
    long countByPostId(String postId);
}