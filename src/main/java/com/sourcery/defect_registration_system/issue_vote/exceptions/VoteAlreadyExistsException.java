package com.sourcery.defect_registration_system.issue_vote.exceptions;

import java.util.UUID;

public class VoteAlreadyExistsException extends RuntimeException {
    public VoteAlreadyExistsException(UUID issueId, UUID userId) {
        super("Vote on issue with id " + issueId + " by user with id " + userId + " already exists");
    }
}
