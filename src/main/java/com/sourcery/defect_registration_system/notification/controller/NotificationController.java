    package com.sourcery.defect_registration_system.notification.controller;

    import com.sourcery.defect_registration_system.notification.dto.NotificationDTO;
    import com.sourcery.defect_registration_system.notification.service.NotificationService;
    import com.sourcery.defect_registration_system.user.service.AuthService;
    import org.springframework.security.oauth2.core.user.OAuth2User;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;

    import java.util.List;
    import java.util.UUID;

    @RestController
    @RequestMapping("/api/notifications")
    public class NotificationController {

        private final NotificationService notificationService;
        private final AuthService authService;

        public NotificationController(NotificationService notificationService, AuthService authService) {
            this.notificationService = notificationService;
            this.authService = authService;
        }

        @GetMapping
        public List<NotificationDTO> getNotifications(@AuthenticationPrincipal OAuth2User principal) {
            // Always use authenticated user's ID
            UUID userId = authService.getCurrentUserId(principal);
            return notificationService.getNotificationsForUser(userId);
        }

        @GetMapping("/unread_notification_count")
        public long getUnreadNotificationCount(@AuthenticationPrincipal OAuth2User principal) {
            UUID userId = authService.getCurrentUserId(principal);
            return notificationService.getUnreadCount(userId);
        }

        @GetMapping("/issues/{issueId}")
        public List<NotificationDTO> getNotificationsByIssue(@PathVariable UUID issueId) {
            return notificationService.getNotificationsForIssue(issueId);
        }

        @PostMapping("/mark_all_read")
        public void markAllAsRead(@AuthenticationPrincipal OAuth2User principal) {
            UUID userId = authService.getCurrentUserId(principal);
            notificationService.markAllAsRead(userId);
        }

        @PostMapping("/mark_read/{notificationId}")
        public void markAsRead(
                @AuthenticationPrincipal OAuth2User principal,
                @PathVariable UUID notificationId
        ) {
            UUID userId = authService.getCurrentUserId(principal);
            notificationService.markAsRead(userId, notificationId);
        }
    }
