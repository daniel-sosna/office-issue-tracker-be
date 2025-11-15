package com.sourcery.defect_registration_system.office.dto;

import com.sourcery.defect_registration_system.office.entity.Office;
import com.sourcery.defect_registration_system.office.enums.Country;

import java.util.UUID;

public record OfficeResponse(
        UUID id,
        String title,
        Country country
) {
    public static OfficeResponse from(Office office) {
        return new OfficeResponse(
                office.getId(),
                office.getTitle(),
                office.getCountry()
        );
    }
}
