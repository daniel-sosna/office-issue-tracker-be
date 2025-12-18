package com.sourcery.defect_registration_system.issue.dto;


import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateIssueRequest(
        @Size(min = 3, max = 200, message = "Summary must be between 3 and 200 characters")
        String summary,

        @Size(max = 2000, message = "Description must be less than 2000 characters")
        String description,

        UUID officeId
) {
}
