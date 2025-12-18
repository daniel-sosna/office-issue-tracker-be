package com.sourcery.defect_registration_system.issue_vote.dto;

import com.sourcery.defect_registration_system.issue_vote.entity.Vote;

import java.util.UUID;

public record VoteResponseDto(
        UUID issueId,
        UUID userId
) {
    public static VoteResponseDto from(Vote vote) {
        return new VoteResponseDto(
                vote.getIssueId(),
                vote.getUserId()
        );
    }
}