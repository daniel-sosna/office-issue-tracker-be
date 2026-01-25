package com.sourcery.defect_registration_system.office.entity;

import com.sourcery.defect_registration_system.office.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Office {
    private UUID id;
    private String title;
    private Country country;
    private OffsetDateTime dateCreated;
    private boolean isDeleted;
}
