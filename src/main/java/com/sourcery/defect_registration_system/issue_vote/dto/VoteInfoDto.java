package com.sourcery.defect_registration_system.issue_vote.dto;

public record VoteInfoDto(
        boolean userVoted,
        int voteCount
) {
}