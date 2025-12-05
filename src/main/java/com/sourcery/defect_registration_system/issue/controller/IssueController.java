package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueDetailsResponseDto;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Issues", description = "Issue management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;

    @GetMapping
    public PageResponseDto<IssueResponseDto> getAllIssuesPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID office,
            @RequestParam(required = false) UUID reportedBy,
            @RequestParam(defaultValue = "dateDesc") String sort) {

        return issueService.getAllIssues(status, office, reportedBy, sort, page, size);
    }

    @GetMapping("/{id}")
    public IssueResponseDto getIssueById(@PathVariable("id") UUID id) {
        return issueService.getIssueById(id);
    }

    @GetMapping("/{id}/details")
    public IssueDetailsResponseDto getIssueDetailsById(@PathVariable("id") UUID id) {
        return issueService.getIssueDetailsById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDto createIssue(@AuthenticationPrincipal OAuth2User principal, @RequestBody @Valid CreateIssueRequest request) {
        return issueService.createIssue(request, principal);
    }

    @PutMapping("/{id}")
    public IssueResponseDto updateIssue(@PathVariable("id") UUID id,
                                        @AuthenticationPrincipal OAuth2User principal,
                                        @Valid @RequestBody UpdateIssueRequest request) {
        return issueService.updateIssue(id, request, principal);
    }

    @PatchMapping("/{id}/status")
    public IssueResponseDto updateIssueStatus(@PathVariable("id") UUID id,
                                              @RequestBody @Valid ChangeIssueStatusRequest request,
                                              @AuthenticationPrincipal OAuth2User principal) {
        return issueService.updateIssueStatus(id, request, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable("id") UUID id,
                            @AuthenticationPrincipal OAuth2User principal) {
        issueService.deleteIssue(id, principal);
    }
}
