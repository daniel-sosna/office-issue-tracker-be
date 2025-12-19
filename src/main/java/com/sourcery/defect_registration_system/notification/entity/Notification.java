package com.sourcery.defect_registration_system.notification.entity;

import com.sourcery.defect_registration_system.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private UUID id;
    private UUID userId;
    private UUID issueId;
    private NotificationType type;
    private String message;
    private boolean readFlag;
    private OffsetDateTime createdAt;
}
