package com.sourcery.defect_registration_system.issue.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateIssueOfficeRequest(
        @NotNull UUID officeId
) {}

