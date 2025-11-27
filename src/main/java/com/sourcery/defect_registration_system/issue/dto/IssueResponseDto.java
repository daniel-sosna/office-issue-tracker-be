package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IssueResponseDto(
        UUID id,
        String summary,
        String description,
        UUID officeId,
        IssueStatus status,
        int votes,
        int comments,
        OffsetDateTime date
) {
    public static IssueResponseDto from(Issue issue, int votes, int comments) {
        return new IssueResponseDto(
                issue.getId(),
                issue.getSummary(),
                issue.getDescription(),
                issue.getOfficeId(),
                issue.getStatus(),
                votes,
                comments,
                issue.getDateCreated()
        );
    }
}