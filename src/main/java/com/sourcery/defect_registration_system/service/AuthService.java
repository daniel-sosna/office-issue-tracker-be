package com.sourcery.defect_registration_system.service;

import com.sourcery.defect_registration_system.exception.BadRequestException;
import com.sourcery.defect_registration_system.exception.NotFoundException;
import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.entity.user.User;
import com.sourcery.defect_registration_system.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private String defaultPicture;

    @Transactional(readOnly = true)
    public Map<String, Object> getCurrentUserInfo(OAuth2User principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication is required.");
        }

        String email = principal.getAttribute("email");
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Email attribute is missing from OAuth2 principal.");
        }

        String picture = principal.getAttribute("picture");
        if (!StringUtils.hasText(picture)) {
            picture = defaultPicture;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));

        return Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "role", user.getRole(),
                "picture", picture
        );
    }
}