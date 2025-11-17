package com.sourcery.defect_registration_system.user.controller;

import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/api/user")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(authService.getCurrentUserInfo(principal));
    }
}
