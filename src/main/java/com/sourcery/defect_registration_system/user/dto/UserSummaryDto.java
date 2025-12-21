package com.sourcery.defect_registration_system.user.dto;

import java.util.UUID;

public record UserSummaryDto(
        UUID id,
        String name
){
}
