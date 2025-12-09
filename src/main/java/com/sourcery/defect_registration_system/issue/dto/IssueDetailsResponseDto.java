package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.attachment.dto.IssueAttachmentResponse;

import java.util.List;

public record IssueDetailsResponseDto(
        IssueResponseDto issue,
        String officeName,
        String officeId,
        String reportedBy,
        String reportedByAvatar,
        String reportedByEmail,
        List<IssueAttachmentResponse> attachments
) {
}
