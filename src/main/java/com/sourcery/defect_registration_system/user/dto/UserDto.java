package com.sourcery.defect_registration_system.user.dto;

import com.sourcery.defect_registration_system.user.enums.Role;

public record UserDto(
        String email,
        String name,
        Role role,
        String picture
) {
}
