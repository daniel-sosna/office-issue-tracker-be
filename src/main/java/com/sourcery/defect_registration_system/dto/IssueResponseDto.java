package com.sourcery.defect_registration_system.dto;

import com.sourcery.defect_registration_system.entity.Issue;
import com.sourcery.defect_registration_system.enums.IssueStatus;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IssueResponseDto {
  UUID id;
  String summary;
  String description;
  String office;
  IssueStatus status;

  public static IssueResponseDto from(Issue issue) {
    return IssueResponseDto.builder()
        .id(issue.getId())
        .summary(issue.getSummary())
        .description(issue.getDescription())
        .office(issue.getOffice())
        .status(issue.getStatus())
        .build();
  }
}
