package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
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
}
