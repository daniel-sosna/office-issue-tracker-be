package com.sourcery.defect_registration_system.profile.exceptions;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(UUID userId) {
        super("Profile not found for userId: " + userId);
    }
}
