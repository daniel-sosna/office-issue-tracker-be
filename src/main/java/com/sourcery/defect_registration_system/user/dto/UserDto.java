package com.sourcery.defect_registration_system.user.dto;

import com.sourcery.defect_registration_system.user.enums.Role;

import java.util.Map;

public record UserDto(
        String email,
        String name,
        Role role,
        String picture
) {
    public Map<String, Object> toMap() {
        return Map.of("email", email, "name", name, "role", role, "picture", picture);
    }
}
