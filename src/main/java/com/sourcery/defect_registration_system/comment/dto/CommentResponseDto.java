package com.sourcery.defect_registration_system.comment.dto;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponseDto {
    private String userName;
    private String imageUrl;
    private String commentText;
    private OffsetDateTime creationDateTime;
}
