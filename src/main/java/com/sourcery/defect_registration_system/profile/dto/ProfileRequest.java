package com.sourcery.defect_registration_system.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record ProfileRequest(

        @Size(min = 2, max = 255)
        @Schema(example = "Alice Smith")
        String name,

        @Size(min = 2, max = 100)
        @Schema(example = "Technology")
        String department,

        @Size(min = 2, max = 50)
        @Schema(example = "Software Engineer")
        String role,

        @Size(min = 5, max = 255)
        @Schema(example = "Main Street")
        String streetAddress,

        @Size(min = 2, max = 100)
        @Schema(example = "Vilnius")
        String city,

        @Size(min = 2, max = 100)
        @Schema(example = "Vilnius County")
        String stateProvince,

        @Size(min = 2, max = 20)
        @Schema(example = "098761")
        String postcode,

        @Size(min = 2, max = 50)
        @Schema(example = "Lithuania")
        String country
) {}