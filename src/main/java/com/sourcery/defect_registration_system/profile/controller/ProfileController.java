package com.sourcery.defect_registration_system.profile.controller;

import com.sourcery.defect_registration_system.profile.dto.ProfileRequest;
import com.sourcery.defect_registration_system.profile.dto.ProfileResponse;
import com.sourcery.defect_registration_system.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile", description = "User profile management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Get logged-in user profile",
            description = "Returns profile details for the current authenticated user. " +
                    "Includes values from both `users` and `user_profile` tables."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile returned successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileResponse> getMyProfile(
            @AuthenticationPrincipal OAuth2User principal
    ) {
        return ResponseEntity.ok(profileService.getMyProfile(principal));
    }

    @Operation(
            summary = "Update logged-in user profile",
            description = "Updates the user profile. " +
                    "If the profile doesn't exist yet, it will be created automatically."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input – validation errors"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @Valid @RequestBody ProfileRequest request,
            @AuthenticationPrincipal OAuth2User principal
    ) {
        return ResponseEntity.ok(profileService.updateMyProfile(principal, request));
    }
}
