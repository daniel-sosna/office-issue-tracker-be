package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.*;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;

    @GetMapping
    public PageResponseDto<IssueResponseDto> getAllIssuesPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return issueService.getAllIssues(page, size);
    }

    @GetMapping("/{id}")
    public IssueResponseDto getIssueById(@PathVariable("id") UUID id) {
        return issueService.getIssueById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDto createIssue(@AuthenticationPrincipal OAuth2User principal, @RequestBody @Valid CreateIssueRequest request) {
        return issueService.createIssue(request, principal);
    }


    //    @Operation(
//            summary = "Update an existing issue",
//            description = "Allows the issue creator to update summary, description and office. Only the owner can modify their own issue."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Issue updated successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid request body or validation errors"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
//            @ApiResponse(responseCode = "403", description = "Forbidden – you can only edit your own issues"),
//            @ApiResponse(responseCode = "404", description = "Issue not found")
//    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDto updateIssue(@PathVariable("id") UUID id, @AuthenticationPrincipal OAuth2User principal, @Valid @RequestBody UpdateIssueRequest request) {
        return issueService.updateIssue(id, request, principal);
    }

//    @Operation(
//            summary = "Change issue status",
//            description = "Allows coordinator to change the status of any issue. Regular users cannot use this endpoint."
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Issue status updated successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid status value"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized – authentication required"),
//            @ApiResponse(responseCode = "403", description = "Forbidden – only coordinator can change status"),
//            @ApiResponse(responseCode = "404", description = "Issue not found")
//    })
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDto updateIssueStatus(@PathVariable("id") UUID id, @RequestBody @Valid ChangeIssueStatusRequest request, @AuthenticationPrincipal OAuth2User principal) {
        return issueService.updateIssueStatus(id, request, principal);
    }
}
