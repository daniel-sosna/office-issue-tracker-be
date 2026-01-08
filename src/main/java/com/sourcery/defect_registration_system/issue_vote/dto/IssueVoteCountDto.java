package com.sourcery.defect_registration_system.issue_vote.dto;

import java.util.UUID;

public record IssueVoteCountDto(
        UUID issueId,
        int voteCount
) {
}