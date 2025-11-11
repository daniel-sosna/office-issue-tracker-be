package com.sourcery.defect_registration_system.controller;

import com.sourcery.defect_registration_system.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(authService.getCurrentUserInfo(principal));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of("1", "ADMIN info"));
    }
}
