package com.sourcery.defect_registration_system.office.exceptions;

public class OfficeInUseException extends RuntimeException {
    public OfficeInUseException(String message) {
        super(message);
    }
}
