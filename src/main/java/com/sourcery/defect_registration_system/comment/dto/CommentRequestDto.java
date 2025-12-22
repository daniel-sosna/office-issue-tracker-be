package com.sourcery.defect_registration_system.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record CommentRequestDto (
    @NotBlank
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    String commentText
){ }
