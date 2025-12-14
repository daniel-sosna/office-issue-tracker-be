package com.sourcery.defect_registration_system.notification.controller;

import com.sourcery.defect_registration_system.notification.dto.NotificationDTO;
import com.sourcery.defect_registration_system.notification.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

        @GetMapping
        public  List<NotificationDTO> getNotifications(
                @RequestParam UUID userId
        ){
            return notificationService.getNotificationsForUser(userId);
        }

            @GetMapping("/unread_notification_count")
        long getUnreadNotificationCount(
                  @RequestParam UUID userId
        ){
            return notificationService.getUnreadCount(userId);
        }

        @PostMapping("/mark_all_read")
        public void markAllAsRead(
                @RequestParam UUID userId
        ){
             notificationService.markAllAsRead(userId);
        }

}
