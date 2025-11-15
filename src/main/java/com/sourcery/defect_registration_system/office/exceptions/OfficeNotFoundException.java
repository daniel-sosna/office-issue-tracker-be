package com.sourcery.defect_registration_system.office.exceptions;

public class OfficeNotFoundException extends RuntimeException {
    public OfficeNotFoundException(String message) {
        super(message);
    }
}
