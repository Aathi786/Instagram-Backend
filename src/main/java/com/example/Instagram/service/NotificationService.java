package com.example.Instagram.service;

import com.example.Instagram.model.Notification;
import com.example.Instagram.model.NotificationResponse;
import com.example.Instagram.model.User;
import com.example.Instagram.repository.NotificationRepository;
import com.example.Instagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    // =========================
    // CREATE NOTIFICATION
    // =========================

    public Notification createNotification(
            Notification notification
    ) {

        notification.setRead(false);
        notification.setCreatedAt(
                LocalDateTime.now()
        );

        return notificationRepository.save(
                notification
        );
    }


    // =========================
    // GET USER NOTIFICATIONS
    // =========================

    public List<NotificationResponse> getUserNotifications(
            String userId,
            String currentUserId
    ) {

        // Security check
        if (!userId.equals(currentUserId)) {
            throw new RuntimeException(
                    "You can only view your own notifications"
            );
        }

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                userId
                        );

        List<NotificationResponse> responses =
                new ArrayList<>();

        for (Notification notification : notifications) {

            NotificationResponse response =
                    new NotificationResponse();

            response.setId(
                    notification.getId()
            );

            response.setUserId(
                    notification.getUserId()
            );

            response.setSenderId(
                    notification.getSenderId()
            );

            response.setType(
                    notification.getType()
            );

            response.setMessage(
                    notification.getMessage()
            );

            response.setPostId(
                    notification.getPostId()
            );

            response.setRead(
                    notification.isRead()
            );

            response.setCreatedAt(
                    notification.getCreatedAt()
            );


            // Get sender details
            User sender = userRepository
                    .findById(
                            notification.getSenderId()
                    )
                    .orElse(null);

            if (sender != null) {

                response.setSenderUsername(
                        sender.getUsername()
                );

                response.setSenderProfilePic(
                        sender.getProfilePic()
                );
            }

            responses.add(response);
        }

        return responses;
    }


    // =========================
    // GET UNREAD COUNT
    // =========================

    public long getUnreadCount(
            String userId,
            String currentUserId
    ) {

        // Security check
        if (!userId.equals(currentUserId)) {
            throw new RuntimeException(
                    "You can only view your own notification count"
            );
        }

        return notificationRepository
                .countByUserIdAndReadFalse(
                        userId
                );
    }


    // =========================
    // MARK AS READ
    // =========================

    public void markAsRead(
            String id,
            String currentUserId
    ) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );


        // Ownership check
        if (!notification
                .getUserId()
                .equals(currentUserId)) {

            throw new RuntimeException(
                    "You can only update your own notification"
            );
        }


        notification.setRead(true);

        notificationRepository.save(
                notification
        );
    }


    // =========================
    // DELETE NOTIFICATION
    // =========================

    public void deleteNotification(
            String id,
            String currentUserId
    ) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );


        // Ownership check
        if (!notification
                .getUserId()
                .equals(currentUserId)) {

            throw new RuntimeException(
                    "You can only delete your own notification"
            );
        }


        notificationRepository.deleteById(id);
    }
}