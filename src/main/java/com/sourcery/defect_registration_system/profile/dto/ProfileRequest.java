package com.sourcery.defect_registration_system.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @Size(min = 2, max = 255)
    @Schema(example = "Alice Smith")
    private String name;

    @Size(min = 2, max = 100)
    @Schema(example = "Technology")
    private String department;

    @Size(min = 2, max = 50)
    @Schema(example = "Software Engineer")
    private String role;

    @Size(min = 5, max = 255)
    @Schema(example = "Main Street")
    private String streetAddress;

    @Size(min = 2, max = 100)
    @Schema(example = "Vilnius")
    private String city;

    @Size(min = 2, max = 100)
    @Schema(example = "Vilnius County")
    private String stateProvince;

    @Size(min = 2, max = 20)
    @Schema(example = "098761")
    private String postcode;

    @Size(min = 2, max = 50)
    @Schema(example = "Lithuania")
    private String country;
}
