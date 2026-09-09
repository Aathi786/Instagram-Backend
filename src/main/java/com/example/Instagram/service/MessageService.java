package com.example.Instagram.service;

import com.example.Instagram.model.Message;
import com.example.Instagram.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {

        if (message.getSenderId() == null ||
                message.getSenderId().isBlank()) {

            throw new RuntimeException("Sender ID is required");
        }

        if (message.getReceiverId() == null ||
                message.getReceiverId().isBlank()) {

            throw new RuntimeException("Receiver ID is required");
        }

        if (message.getText() == null ||
                message.getText().isBlank()) {

            throw new RuntimeException("Message text is required");
        }

        message.setText(message.getText().trim());
        message.setRead(false);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }
    public long getUnreadCount(String userId) {

        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }
    public void markConversationAsRead(
            String receiverId,
            String senderId) {

        System.out.println("========== MARK AS READ SERVICE ==========");
        System.out.println("Receiver ID : " + receiverId);
        System.out.println("Sender ID   : " + senderId);

        List<Message> messages =
                messageRepository.findByReceiverIdAndSenderIdAndReadFalse(
                        receiverId,
                        senderId
                );

        System.out.println("Unread messages found: " + messages.size());

        for (Message message : messages) {

            System.out.println(
                    "Marking message as READ: " + message.getId()
            );

            message.setRead(true);
        }

        messageRepository.saveAll(messages);

        System.out.println("Messages saved as READ");
        System.out.println("==========================================");
    }
    public long getConversationUnreadCount(
            String receiverId,
            String senderId) {

        return messageRepository
                .countByReceiverIdAndSenderIdAndReadFalse(
                        receiverId,
                        senderId
                );
    }

    public List<Message> getConversation(
            String senderId,
            String receiverId) {

        return messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
                        senderId,
                        receiverId,
                        receiverId,
                        senderId
                );
    }

    public void deleteMessage(
            String id,
            String currentUserId
    ) {

        Message message = messageRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Message not found")
                );

        // Only the sender can delete their own message
        if (!message.getSenderId().equals(currentUserId)) {
            throw new RuntimeException(
                    "You can only delete your own message"
            );
        }

        messageRepository.deleteById(id);
    }


}