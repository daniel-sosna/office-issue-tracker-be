package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueDetailsResponseDto;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueOfficeRequest;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
            @RequestParam(defaultValue = "dateDesc") String sort,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        return issueService.getAllIssues(status, office, reportedBy, sort, page, size, principal);
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
            summary = "Get issue details by ID",
            description = "Returns the issue with more details for the given UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
            @ApiResponse(responseCode = "404", description = "Issue not found")
    })
    @GetMapping("/{id}/details")
    public IssueDetailsResponseDto getIssueDetailsById(@PathVariable("id") UUID id) {
        return issueService.getIssueDetailsById(id);
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDto createIssue(@AuthenticationPrincipal OAuth2User principal,
                                        @Parameter(description = "Issue data in JSON format", required = true)
                                        @RequestPart("issue") @Valid CreateIssueRequest request,
                                        @Parameter(description = "Attachment files (optional)", required = false)
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return issueService.createIssue(request, files, principal);
    }

    @Operation(
            summary = "Update an existing issue",
            description = "Allows the issue creator to update summary, description and office. Only the owner can modify their own issue."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation errors"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden – you can only edit your own issues"),
            @ApiResponse(responseCode = "404", description = "Issue not found")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDto updateIssue(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal OAuth2User principal,
            @RequestPart("issue") @Valid UpdateIssueRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "deleteAttachmentIds", required = false) List<UUID> deleteAttachmentIds) {
        return issueService.updateIssue(id, request, files, deleteAttachmentIds, principal);
    }

    @Operation(
            summary = "Change issue status",
            description = "Allows coordinator to change the status of any issue. Regular users cannot use this endpoint."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden – only coordinator can change status"),
            @ApiResponse(responseCode = "404", description = "Issue not found")
    })
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public IssueResponseDto updateIssueStatus(@PathVariable("id") UUID id, @RequestBody @Valid ChangeIssueStatusRequest request, @AuthenticationPrincipal OAuth2User principal) {
        return issueService.updateIssueStatus(id, request, principal);
    }

    @Operation(
            summary = "Soft delete an issue",
            description = "Changes the status of the issue to DELETED."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Issue successfully soft-deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only coordinator/owner can soft-delete this issue"),
            @ApiResponse(responseCode = "404", description = "Issue not found")
    })
    @PatchMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void softDeleteIssue(@PathVariable("id") UUID id, @AuthenticationPrincipal OAuth2User principal) {
        issueService.softDeleteIssue(id, principal);
    }
    @PatchMapping("/{id}/office")
    public void updateIssueOffice(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateIssueOfficeRequest request,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        issueService.updateIssueOffice(id, request.officeId(), principal);
    }

}
