package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PageIssueResponseDto(
        UUID id,
        String summary,
        String description,
        IssueStatus status,
        OffsetDateTime dateCreated,
        boolean hasVoted,
        int voteCount
) {
    public static PageIssueResponseDto from(Issue issue, boolean hasVoted, int voteCount) {
        return new PageIssueResponseDto(
                issue.getId(),
                issue.getSummary(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getDateCreated(),
                hasVoted,
                voteCount
        );
    }
}