package com.sourcery.defect_registration_system.attachment.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadIssueAttachmentRequest(

        @NotNull(message = "File cannot be null")
        List<MultipartFile> files
) {
}
