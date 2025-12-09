package com.sourcery.defect_registration_system.issue_vote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
    private UUID issueId;
    private UUID userId;
}
