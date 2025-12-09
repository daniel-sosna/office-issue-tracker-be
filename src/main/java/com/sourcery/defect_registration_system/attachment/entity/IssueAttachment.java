package com.sourcery.defect_registration_system.attachment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueAttachment {
    private UUID id;
    private UUID issueId;
    private UUID uploadedBy;
    private String publicId;
    private String url;
    private String format;
    private String originalFilename;
    private long fileSize;
    private OffsetDateTime dateCreated;
}
