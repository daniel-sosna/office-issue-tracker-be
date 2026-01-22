package com.sourcery.defect_registration_system.comment.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentWebSocketDto(
        UUID id,
        UUID issueId,
        String authorName,
        String authorPicture,
        String commentText,
        OffsetDateTime createdAt
) {}