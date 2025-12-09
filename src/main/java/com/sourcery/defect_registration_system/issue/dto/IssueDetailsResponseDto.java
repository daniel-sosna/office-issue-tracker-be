package com.sourcery.defect_registration_system.issue.dto;

public record IssueDetailsResponseDto(
        IssueResponseDto issue,
        String officeName,
        String officeId,
        String reportedBy,
        String reportedByAvatar,
        String reportedByEmail
) {
}
