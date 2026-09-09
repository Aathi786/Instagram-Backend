package com.example.Instagram.repository;

import com.example.Instagram.model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends MongoRepository<Story, String> {

    List<Story> findByExpiresAtAfter(LocalDateTime now);


    List<Story> findByUserIdAndExpiresAtAfter(
            String userId,
            LocalDateTime now
    );
    List<Story> findByExpiresAtBefore(LocalDateTime now);
}