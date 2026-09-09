package com.example.Instagram.controller;

import com.example.Instagram.model.Message;
import com.example.Instagram.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping
    public Message sendMessage(
            @RequestBody Message message,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        message.setSenderId(currentUserId);

        return messageService.sendMessage(message);
    }


    @GetMapping("/conversation")
    public List<Message> getConversation(
            @RequestParam String senderId,
            @RequestParam String receiverId) {

        return messageService.getConversation(
                senderId,
                receiverId
        );
    }

    @DeleteMapping("/{id}")
    public String deleteMessage(
            @PathVariable String id,
            Authentication authentication
    ) {

        String currentUserId = authentication.getName();

        messageService.deleteMessage(
                id,
                currentUserId
        );

        return "Message deleted successfully";
    }


    @GetMapping("/unread/{userId}")
    public long getUnreadCount(
            @PathVariable String userId) {

        return messageService.getUnreadCount(userId);
    }
    @PutMapping("/read")
    public String markConversationAsRead(
            @RequestParam String receiverId,
            @RequestParam String senderId) {

        System.out.println("========== MARK AS READ CONTROLLER ==========");
        System.out.println("Receiver ID : " + receiverId);
        System.out.println("Sender ID   : " + senderId);

        messageService.markConversationAsRead(
                receiverId,
                senderId
        );

        System.out.println("=============================================");

        return "Messages marked as read";
    }
    @GetMapping("/unread/conversation")
    public long getConversationUnreadCount(
            @RequestParam String receiverId,
            @RequestParam String senderId) {

        return messageService.getConversationUnreadCount(
                receiverId,
                senderId
        );
    }
}