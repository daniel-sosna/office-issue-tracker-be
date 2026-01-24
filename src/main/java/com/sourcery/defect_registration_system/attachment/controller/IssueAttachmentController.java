package com.sourcery.defect_registration_system.attachment.controller;


import com.sourcery.defect_registration_system.attachment.service.IssueAttachmentService;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.service.AuthService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Attachments", description = "Attachment management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class IssueAttachmentController {


    private final IssueAttachmentService issueAttachmentService;
    private final AuthService authService;


    @Operation(
            summary = "Delete an attachment",
            description = "Deletes an attachment by its ID. Only the creator can delete the attachment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only owner can delete this attachment"),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttachment(
            @PathVariable UUID id,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        UserDto user = authService.getCurrentUserInfo(principal);
        issueAttachmentService.deleteAttachment(id, user.id());
    }
}
