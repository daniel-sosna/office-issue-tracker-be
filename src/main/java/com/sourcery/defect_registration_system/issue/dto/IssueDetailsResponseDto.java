package com.sourcery.defect_registration_system.issue.dto;

public record IssueDetailsResponseDto(
        IssueResponseDto issue,
        String officeName,
        String reportedBy,
        String reportedByAvatar,
        String reportedByEmail
) {
}
