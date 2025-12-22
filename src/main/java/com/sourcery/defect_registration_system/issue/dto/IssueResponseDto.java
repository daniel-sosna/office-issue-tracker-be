package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IssueResponseDto(
        UUID id,
        String summary,
        String description,
        IssueStatus status,
        OffsetDateTime dateCreated,
        OffsetDateTime dateModified,
        int voteCount
) {
    public static IssueResponseDto from(Issue issue) {
        return new IssueResponseDto(
                issue.getId(),
                issue.getSummary(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getDateCreated(),
                issue.getDateModified() == null ? null : issue.getDateModified(),
                issue.getVoteCount()
        );
    }
}