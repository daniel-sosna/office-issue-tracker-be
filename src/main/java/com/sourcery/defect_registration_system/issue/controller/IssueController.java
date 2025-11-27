package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
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

    @Operation(
            summary = "Get paginated list of issues",
            description = "Returns a paginated list of issues with page and size parameters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of issues returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
    })
    @GetMapping
    public PageResponseDto<IssueResponseDto> getAllIssuesPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return issueService.getAllIssues(page, size);
    }

    @Operation(
            summary = "Get issue by ID",
            description = "Returns the issue details for the given UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
            @ApiResponse(responseCode = "404", description = "Issue not found")
    })
    @GetMapping("/{id}")
    public IssueResponseDto getIssueById(@PathVariable("id") UUID id) {
        return issueService.getIssueById(id);
    }

    @Operation(
            summary = "Create a new issue",
            description = "Creates a new issue using the authenticated user's identity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Issue created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable("id") UUID id, @AuthenticationPrincipal OAuth2User principal) {
        issueService.deleteIssue(id, principal);
    }

}
