package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.attachment.dto.IssueAttachmentResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record IssueDetailsResponseDto(
        IssueResponseDto issue,
        UUID officeId,
        String officeName,
        String reportedBy,
        String reportedByAvatar,
        List<IssueAttachmentResponse> attachments
) {
}
