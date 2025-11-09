package com.sourcery.defect_registration_system.config.security;

import com.sourcery.defect_registration_system.identity.user.User;
import com.sourcery.defect_registration_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, Object>> getUserInto(OAuth2AuthenticationToken auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String email = auth.getPrincipal().getAttribute("email");
        String picture = auth.getPrincipal().getAttribute("picture");
        if (picture == null) {
            picture = "default.png";
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> map = Map.of("email", user.getEmail(),
                "picture", picture,
                "name", user.getName(),
                "role", user.getRole()
        );

        return ResponseEntity.ok(map);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/api/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        var map = Map.of("1", "ADMIN info");
        return ResponseEntity.ok(map);
    }

}
