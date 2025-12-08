package com.sourcery.defect_registration_system.attachment.dto;

import java.util.UUID;

public record IssueAttachmentResponse(
        UUID id,
        String url,
        String format,
        String originalFilename,
        long fileSize
) {
}
