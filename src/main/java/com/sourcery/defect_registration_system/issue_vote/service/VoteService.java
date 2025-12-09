package com.sourcery.defect_registration_system.issue_vote.service;

import com.sourcery.defect_registration_system.issue_vote.dto.CreateVoteRequestDto;
import com.sourcery.defect_registration_system.issue_vote.dto.VoteResponseDto;
import com.sourcery.defect_registration_system.issue_vote.entity.Vote;
import com.sourcery.defect_registration_system.issue_vote.exceptions.VoteAlreadyExistsException;
import com.sourcery.defect_registration_system.issue_vote.exceptions.VoteNotFoundException;
import com.sourcery.defect_registration_system.issue_vote.repository.VoteRepository;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuthService authService;

    public boolean isVoteExist(Vote vote) {

        return voteRepository.isVoteExists(vote.getIssueId(), vote.getUserId());
    }

    public List<VoteResponseDto> getAllVotes() {

        return voteRepository.getAllVotes().stream()
                .map(VoteResponseDto::from)
                .toList();
    }

    @Transactional
    public VoteResponseDto createVote(CreateVoteRequestDto request, OAuth2User principal) {

        UUID userId = authService.getCurrentUserId(principal);

        if (voteRepository.isVoteExists(request.issueId(), userId)) {
            throw new VoteAlreadyExistsException(request.issueId(), userId);
        }

        Vote vote = Vote.builder()
                .issueId(request.issueId())
                .userId(userId)
                .build();

        voteRepository.insertVote(vote);

        return VoteResponseDto.from(vote);
    }

    @Transactional
    public void deleteVote(UUID issueId, OAuth2User principal) {

        UUID userId = authService.getCurrentUserId(principal);

        if (!voteRepository.isVoteExists(issueId, userId)) {
            throw new VoteNotFoundException(issueId, userId);
        }

        voteRepository.deleteVote(issueId, userId);
    }

}