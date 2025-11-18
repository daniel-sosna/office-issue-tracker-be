package com.sourcery.defect_registration_system.user.service;

import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.exception.BadRequestException;
import com.sourcery.defect_registration_system.exception.NotFoundException;
import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto getCurrentUserInfo(OAuth2User principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication is required.");
        }

        String email = principal.getAttribute("email");
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Email attribute is missing from OAuth2 principal.");
        }

        String picture = principal.getAttribute("picture");
        if (!StringUtils.hasText(picture)) {
            picture = "https://ui-avatars.com/api/?name=User&background=CCCCCC&color=555555&size=256";
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));
        return new UserDto(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                picture
        );
    }

    @Transactional(readOnly = true)
    public UUID getCurrentUserId(OAuth2User principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication is required.");
        }

        String email = principal.getAttribute("email");
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Email attribute is missing from OAuth2 principal.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));

        return user.getId();
    }
}