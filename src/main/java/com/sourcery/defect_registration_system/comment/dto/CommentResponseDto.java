package com.sourcery.defect_registration_system.comment.dto;

import java.time.OffsetDateTime;

public record CommentResponseDto(
    String userName,
    String imageUrl,
    String commentText,
    OffsetDateTime creationDateTime
) {}
