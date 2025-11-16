package com.sourcery.defect_registration_system.issue.dto;

import java.util.List;

public record PageResponseDto<T>(
    List<T> content,
    long totalElements,
    long totalPages,
    int page,
    int size
) {}
