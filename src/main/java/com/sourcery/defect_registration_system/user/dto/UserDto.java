package com.sourcery.defect_registration_system.user.dto;

import com.sourcery.defect_registration_system.user.enums.Role;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String name,
        Role role,
        String picture
) {
}
