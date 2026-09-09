package com.example.Instagram.controller;

import com.example.Instagram.model.Notification;
import com.example.Instagram.model.NotificationResponse;
import com.example.Instagram.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {

        this.notificationService =
                notificationService;
    }


    // =========================
    // CREATE NOTIFICATION
    // =========================

    @PostMapping
    public Notification createNotification(
            @RequestBody Notification notification,
            Authentication authentication
    ) {

        // Get logged-in user from JWT
        String currentUserId =
                authentication.getName();

        // Never trust senderId from frontend
        notification.setSenderId(
                currentUserId
        );

        return notificationService
                .createNotification(
                        notification
                );
    }


    // =========================
    // GET NOTIFICATIONS
    // =========================

    @GetMapping("/{userId}")
    public List<NotificationResponse> getUserNotifications(
            @PathVariable String userId,
            Authentication authentication
    ) {

        String currentUserId =
                authentication.getName();

        return notificationService
                .getUserNotifications(
                        userId,
                        currentUserId
                );
    }


    // =========================
    // GET UNREAD COUNT
    // =========================

    @GetMapping("/unread/{userId}")
    public long getUnreadCount(
            @PathVariable String userId,
            Authentication authentication
    ) {

        String currentUserId =
                authentication.getName();

        return notificationService
                .getUnreadCount(
                        userId,
                        currentUserId
                );
    }


    // =========================
    // MARK AS READ
    // =========================

    @PutMapping("/read/{id}")
    public String markAsRead(
            @PathVariable String id,
            Authentication authentication
    ) {

        String currentUserId =
                authentication.getName();

        notificationService
                .markAsRead(
                        id,
                        currentUserId
                );

        return "Notification marked as read";
    }


    // =========================
    // DELETE NOTIFICATION
    // =========================

    @DeleteMapping("/{id}")
    public String deleteNotification(
            @PathVariable String id,
            Authentication authentication
    ) {

        String currentUserId =
                authentication.getName();

        notificationService
                .deleteNotification(
                        id,
                        currentUserId
                );

        return "Notification deleted successfully";
    }
}