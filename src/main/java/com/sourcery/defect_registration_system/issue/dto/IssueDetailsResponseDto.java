package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.attachment.dto.IssueAttachmentResponse;

import java.util.List;
import java.util.UUID;

public record IssueDetailsResponseDto(
        IssueResponseDto issue,
        String officeName,
        UUID officeId,
        String reportedBy,
        String reportedByAvatar,
        String reportedByEmail,
        List<IssueAttachmentResponse> attachments,
        int voteCount
) {
}
