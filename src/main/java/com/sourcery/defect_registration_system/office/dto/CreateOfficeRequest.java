package com.sourcery.defect_registration_system.office.dto;

import com.sourcery.defect_registration_system.office.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOfficeRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotNull(message = "Country is required")
        Country country
) {
}
