package com.sourcery.defect_registration_system.profile.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User not found with userId: " + userId);
    }
}
