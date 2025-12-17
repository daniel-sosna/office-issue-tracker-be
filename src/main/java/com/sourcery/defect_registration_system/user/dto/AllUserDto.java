package com.sourcery.defect_registration_system.user.dto;

import java.util.UUID;

public record AllUserDto (
        UUID id,
        String name
){
}
