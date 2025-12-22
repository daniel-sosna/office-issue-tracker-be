package com.sourcery.defect_registration_system.issue.entity;

import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
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
public class Issue {
  private UUID id;
  private String summary;
  private String description;
  private UUID officeId;
  private IssueStatus status;
  private UUID createdBy;
  private OffsetDateTime dateCreated;
  private OffsetDateTime dateModified;
  private int voteCount;
}
