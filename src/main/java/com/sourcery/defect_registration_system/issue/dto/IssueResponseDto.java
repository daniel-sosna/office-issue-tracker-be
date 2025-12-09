package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.issue.entity.Issue;


public record IssueResponseDto(
        String id,
        String summary,
        String description,
        String status,
        String createdBy,
        String officeId,
        String dateCreated,
        String dateModified
) {
    public static IssueResponseDto from(Issue issue) {
        return new IssueResponseDto(
                issue.getId().toString(),
                issue.getSummary(),
                issue.getDescription(),
                issue.getStatus().name(),
                issue.getCreatedBy().toString(),
                issue.getOfficeId().toString(),
                issue.getDateCreated().toString(),
                issue.getDateModified() == null ? null : issue.getDateModified().toString()

        );
    }
}

