package com.sourcery.defect_registration_system.issue.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponseDto<T> {
  private List<T> content;
  private long totalElements;
  private long totalPages;
  private int page;
  private int size;
}
