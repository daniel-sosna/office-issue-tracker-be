package com.sourcery.defect_registration_system.profile.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User not found with userId: " + userId);
    }
}
