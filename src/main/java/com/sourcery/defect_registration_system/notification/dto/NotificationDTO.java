package com.sourcery.defect_registration_system.notification.dto;

import com.sourcery.defect_registration_system.notification.enums.NotificationType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationDTO(
        UUID id,
        UUID issueId,
        NotificationType type,
        String message,
        boolean readFlag,
        OffsetDateTime createdAt
) {
    public static NotificationDTO fromEntity(com.sourcery.defect_registration_system.notification.entity.Notification notification){
        return new NotificationDTO(
                notification.getId(),
                notification.getIssueId(),
                notification.getType(),
                notification.getMessage(),
                notification.isReadFlag(),
                notification.getCreatedAt()
        );
    }
}
