package com.sourcery.defect_registration_system.profile.dto;

import com.sourcery.defect_registration_system.office.enums.Country;
import com.sourcery.defect_registration_system.profile.entity.Profile;
import com.sourcery.defect_registration_system.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponse(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID userId,

        @Schema(example = "Alice Smith")
        String name,

        @Schema(example = "alice.smith@example.com")
        String email,

        @Schema(example = "path/to/img_url")
        String imageUrl,

        @Schema(example = "Engineering")
        String department,

        @Schema(example = "Software Engineer")
        String role,

        @Schema(example = "123 Main Street")
        String streetAddress,

        @Schema(example = "Vilnius")
        String city,

        @Schema(example = "Vilnius County")
        String stateProvince,

        @Schema(example = "098172")
        String postcode,

        @Schema(example = "LITHUANIA")
        Country country,

        @Schema(example = "2025-12-08T17:05:57.578Z")
        Instant updatedAt
) {
    public static ProfileResponse from(User user, Profile profile, Country countryEnum) {
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getImageUrl(),
                profile.getDepartment(),
                profile.getRole(),
                profile.getStreetAddress(),
                profile.getCity(),
                profile.getStateProvince(),
                profile.getPostcode(),
                countryEnum,
                profile.getUpdatedAt()
        );
    }
}
