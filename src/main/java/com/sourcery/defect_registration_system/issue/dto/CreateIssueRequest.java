package com.sourcery.defect_registration_system.issue.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIssueRequest(

        @NotBlank(message = "Summary is required")
        @Size(min = 3, max = 200, message = "Summary must be between 3 and 200 characters")
        String summary,

        @NotBlank(message = "Description is required")
        @Size(max = 2000, message = "Description must be less than 2000 characters")
        String description,

        @NotNull(message = "Office must be selected")
        UUID office
) {
}
