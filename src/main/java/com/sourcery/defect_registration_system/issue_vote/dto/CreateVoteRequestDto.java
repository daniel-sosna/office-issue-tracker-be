package com.sourcery.defect_registration_system.issue_vote.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateVoteRequestDto(

        @NotBlank(message = "Issue ID is required")
        UUID issueId
) {
}
