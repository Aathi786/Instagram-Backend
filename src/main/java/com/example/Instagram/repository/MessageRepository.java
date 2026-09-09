package com.example.Instagram.repository;

import com.example.Instagram.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository
        extends MongoRepository<Message, String> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
            String senderId,
            String receiverId,
            String receiverId2,
            String senderId2
    );

    long countByReceiverIdAndReadFalse(String receiverId);

    List<Message> findByReceiverIdAndSenderIdAndReadFalse(
            String receiverId,
            String senderId
    );
    long countByReceiverIdAndSenderIdAndReadFalse(
            String receiverId,
            String senderId
    );
}