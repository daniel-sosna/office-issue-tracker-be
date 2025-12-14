package com.sourcery.defect_registration_system.notification.service;

import com.sourcery.defect_registration_system.notification.dto.NotificationDTO;
import com.sourcery.defect_registration_system.notification.entity.Notification;
import com.sourcery.defect_registration_system.notification.enums.NotificationType;
import com.sourcery.defect_registration_system.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void createNotification(UUID userId, UUID issueId, NotificationType type, String message) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .issueId(issueId)
                .type(type)
                .message(message)
                .readFlag(false)
                .createdAt(OffsetDateTime.now())
                .build();

        notificationRepository.insertNotification(notification);

        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",
                NotificationDTO.fromEntity(notification)
        );
    }

    public List<NotificationDTO> getNotificationsForUser(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadNotification(userId);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAsRead(userId);
    }

    public void notifyComment(UUID issueId, UUID issueReporterId, String commenterName) {
        String message = commenterName + " commented on your issue.";
        createNotification(issueReporterId, issueId, NotificationType.COMMENT, message);
    }

    public void notifyUpvote(UUID issueId, UUID issueReporterId, String upvotedBy) {
        String message = upvotedBy + " upvoted your issue.";
        createNotification(issueReporterId, issueId, NotificationType.UPVOTE, message);
    }

    public void notifyStatusChange(UUID issueId, UUID issueReporterId, String adminName, String newStatus) {
        String message = adminName + " changed status to " + newStatus;
        createNotification(issueReporterId, issueId, NotificationType.ISSUE_STATUS_CHANGE, message);
    }
}
