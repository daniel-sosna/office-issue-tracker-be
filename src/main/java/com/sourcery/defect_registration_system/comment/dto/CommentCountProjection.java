package com.sourcery.defect_registration_system.comment.dto;

import java.util.UUID;

public record CommentCountProjection(
    UUID issueId,
    int commentCount
) {}
