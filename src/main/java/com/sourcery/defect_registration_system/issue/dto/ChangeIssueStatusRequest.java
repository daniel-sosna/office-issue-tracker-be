package com.sourcery.defect_registration_system.issue.dto;

import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeIssueStatusRequest(
        @NotNull(message = "Status is required")
        IssueStatus status
) {
}
