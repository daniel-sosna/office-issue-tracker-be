package com.sourcery.defect_registration_system.config.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @GetMapping("/api/user")
    public ResponseEntity<Map<String, Object>> getUserInto(OAuth2AuthenticationToken auth){
        if (auth == null){
            return ResponseEntity.ok(null);
        }
        String email = auth.getPrincipal().getAttribute("email");
        String picture = auth.getPrincipal().getAttribute("picture");
        if (picture == null){
            picture ="default.png";
        }

        Map<String, Object> map = Map.of("email", email, "picture", picture);
        return ResponseEntity.ok(map);
    }
}
