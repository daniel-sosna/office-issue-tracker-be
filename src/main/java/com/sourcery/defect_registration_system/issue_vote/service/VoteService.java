package com.sourcery.defect_registration_system.issue_vote.service;

import com.sourcery.defect_registration_system.issue_vote.dto.IssueVoteCountDto;
import com.sourcery.defect_registration_system.issue_vote.dto.VoteInfoDto;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuthService authService;

    public boolean hasVotedOnIssue(UUID issueId, OAuth2User principal) {

        UUID userId = authService.getCurrentUserId(principal);

        return voteRepository.isVoteExist(issueId, userId);
    }

    public List<VoteResponseDto> getAllVotes() {

        return voteRepository.findAllVotes().stream()
                .map(VoteResponseDto::from)
                .toList();
    }

    @Transactional
    public VoteResponseDto createVote(UUID issueId, OAuth2User principal) {

        UUID userId = authService.getCurrentUserId(principal);

        if (voteRepository.isVoteExist(issueId, userId)) {
            throw new VoteAlreadyExistsException(issueId, userId);
        }

        Vote vote = Vote.builder()
                .issueId(issueId)
                .userId(userId)
                .build();

        voteRepository.insertVote(vote);

        return VoteResponseDto.from(vote);
    }

    @Transactional
    public void deleteVote(UUID issueId, OAuth2User principal) {

        UUID userId = authService.getCurrentUserId(principal);

        if (!voteRepository.isVoteExist(issueId, userId)) {
            throw new VoteNotFoundException(issueId, userId);
        }

        voteRepository.deleteVote(issueId, userId);
    }

    public Map<UUID, VoteInfoDto> getVoteInfoForIssues(List<UUID> issueIds, UUID userId) {

        if (issueIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<UUID, Integer> voteCounts = voteRepository.countVotesOnIssues(issueIds)
                .stream()
                .collect(Collectors.toMap(
                        IssueVoteCountDto::issueId,
                        IssueVoteCountDto::voteCount
                ));

        Set<UUID> votedIssueIds = new HashSet<>(voteRepository.findIssuesVotedByUser(issueIds, userId));

        Map<UUID, VoteInfoDto> voteInfoDtos = new HashMap<>();
        for (UUID id : issueIds) {
            voteInfoDtos.put(
                    id,
                    new VoteInfoDto(
                            votedIssueIds.contains(id),
                            voteCounts.getOrDefault(id, 0)
                    )
            );
        }

        return voteInfoDtos;
    }

}