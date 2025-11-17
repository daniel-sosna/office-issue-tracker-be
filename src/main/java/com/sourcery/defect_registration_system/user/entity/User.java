package com.sourcery.defect_registration_system.user.entity;

import com.sourcery.defect_registration_system.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private String imageUrl;
}
