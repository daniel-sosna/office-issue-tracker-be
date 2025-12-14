package com.sourcery.defect_registration_system.issue_vote.controller;

import com.sourcery.defect_registration_system.issue_vote.dto.VoteResponseDto;
import com.sourcery.defect_registration_system.issue_vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Votes", description = "Vote management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class VoteController {

    private final VoteService voteService;

    @Operation(
            summary = "Check vote by issue ID",
            description = "Returns if the vote exists for the given issue UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vote existence returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @GetMapping("/{id}/vote")
    public boolean checkVote(@PathVariable("id") UUID issueId, @AuthenticationPrincipal OAuth2User principal) {
        return voteService.hasVotedOnIssue(issueId, principal);
    }

    @Operation(
            summary = "Get list of votes",
            description = "Returns list of votes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of votes returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
    })
    @GetMapping("/votes")
    public List<VoteResponseDto> getAllVotes() {
        return voteService.getAllVotes();
    }

    @Operation(
            summary = "Vote for an issue by its ID",
            description = "Create a vote for an issue by given UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @PostMapping("/{id}/vote")
    public VoteResponseDto createVote(@PathVariable("id") UUID issueId, @AuthenticationPrincipal OAuth2User principal) {
        return voteService.createVote(issueId, principal);
    }

    @Operation(
            summary = "Unvote an issue by its ID",
            description = "Delete a vote for an issue by given UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Issue successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – authentication required"),
            @ApiResponse(responseCode = "404", description = "Vote not found")
    })
    @DeleteMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@PathVariable("id") UUID issueId, @AuthenticationPrincipal OAuth2User principal) {
        voteService.deleteVote(issueId, principal);
    }

}
