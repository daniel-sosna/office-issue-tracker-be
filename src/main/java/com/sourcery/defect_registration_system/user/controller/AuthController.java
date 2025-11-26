package com.sourcery.defect_registration_system.user.controller;

import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Get authenticated user info",
            description = "Returns details of the currently authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @GetMapping("/api/user")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(authService.getCurrentUserInfo(principal));
    }
}
