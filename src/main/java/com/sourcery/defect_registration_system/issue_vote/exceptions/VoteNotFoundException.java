package com.sourcery.defect_registration_system.issue_vote.exceptions;

import java.util.UUID;

public class VoteNotFoundException extends RuntimeException {
    public VoteNotFoundException(UUID issueId, UUID userId) {
        super("Vote on issue with id " + issueId + " by user with id " + userId + " not found");
    }
}
