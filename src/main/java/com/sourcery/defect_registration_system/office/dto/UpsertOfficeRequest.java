package com.sourcery.defect_registration_system.office.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpsertOfficeRequest(
        @Nullable
        UUID id,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotNull(message = "Country is required")
        String countryName
) {
}
