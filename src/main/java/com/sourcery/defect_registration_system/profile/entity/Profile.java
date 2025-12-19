package com.sourcery.defect_registration_system.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    private UUID id;
    private UUID userId;
    private String department;
    private String role;
    private String streetAddress;
    private String city;
    private String stateProvince;
    private String postcode;
    private String country;
    private Instant updatedAt;
}
