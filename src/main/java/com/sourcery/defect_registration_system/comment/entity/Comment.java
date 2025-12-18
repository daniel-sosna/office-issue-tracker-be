package com.sourcery.defect_registration_system.comment.entity;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private UUID id;
    private UUID userId;
    private UUID issueId;
    private String commentText;
    private OffsetDateTime dateCreated;
}
