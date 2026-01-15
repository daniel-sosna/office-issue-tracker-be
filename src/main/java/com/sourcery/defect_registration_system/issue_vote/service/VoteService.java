package com.sourcery.defect_registration_system.issue_vote.service;

import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.exception.BadRequestException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.issue_vote.dto.IssueVoteCountDto;
import com.sourcery.defect_registration_system.issue_vote.dto.VoteInfoDto;
import com.sourcery.defect_registration_system.issue_vote.dto.VoteResponseDto;
import com.sourcery.defect_registration_system.issue_vote.entity.Vote;
import com.sourcery.defect_registration_system.issue_vote.exceptions.VoteAlreadyExistsException;
import com.sourcery.defect_registration_system.issue_vote.exceptions.VoteNotFoundException;
import com.sourcery.defect_registration_system.issue_vote.repository.VoteRepository;
import com.sourcery.defect_registration_system.user.service.AuthService;
import com.sourcery.defect_registration_system.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuthService authService;
    private final IssueRepository issueRepository;
    private final NotificationService notificationService;
    private final IssueRepository issueRepository;

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

        issueRepository.getIssueById(issueId).ifPresent(issue -> {
            if (issue.getCreatedBy().equals(userId)) {
                throw new BadRequestException("Creator of the issue cannot vote for it");
            }
        });

        Vote vote = Vote.builder()
                .issueId(issueId)
                .userId(userId)
                .build();

        voteRepository.insertVote(vote);

        Issue issue = issueRepository.getIssueById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue with ID " + issueId + " not found"));
        UUID issueReporterId = issue.getCreatedBy();
        String upvotedBy = authService.getCurrentUserInfo(principal).name();
        notificationService.notifyUpvote(issueId, issueReporterId, upvotedBy);

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