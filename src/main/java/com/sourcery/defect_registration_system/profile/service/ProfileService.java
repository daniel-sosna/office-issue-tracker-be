package com.sourcery.defect_registration_system.profile.service;

import com.sourcery.defect_registration_system.profile.dto.ProfileRequest;
import com.sourcery.defect_registration_system.profile.dto.ProfileResponse;
import com.sourcery.defect_registration_system.profile.entity.Profile;
import com.sourcery.defect_registration_system.profile.exceptions.InvalidProfileDataException;
import com.sourcery.defect_registration_system.profile.repository.ProfileRepository;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileResponse getMyProfile(OAuth2User principal) {
        UUID userId = getUserIdFromPrincipal(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidProfileDataException("User not found for id: " + userId));

        Profile profile = profileRepository.findProfileByUserId(userId)
                .orElseGet(() -> Profile.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        return ProfileResponse.from(user, profile);
    }

    @Transactional
    public ProfileResponse updateMyProfile(OAuth2User principal, ProfileRequest request) {
        UUID userId = getUserIdFromPrincipal(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidProfileDataException("User not found for id: " + userId));

        if (request.getName() != null && !request.getName().isBlank()) {
            userRepository.updateUserName(userId, request.getName());
            user.setName(request.getName());
        }

        Profile profile = profileRepository.findProfileByUserId(userId)
                .orElseGet(() -> Profile.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        profile.setDepartment(request.getDepartment());
        profile.setRole(request.getRole());
        profile.setStreetAddress(request.getStreetAddress());
        profile.setCity(request.getCity());
        profile.setStateProvince(request.getStateProvince());
        profile.setPostcode(request.getPostcode());

        if (request.getCountry() != null) {
            profile.setCountry(request.getCountry().toUpperCase());
        }

        if (profileRepository.existsByUserId(userId)) {
            profileRepository.updateProfile(profile);
        } else {
            profileRepository.insertProfile(profile);
        }

        return ProfileResponse.from(user, profile);
    }

    private UUID getUserIdFromPrincipal(OAuth2User principal) {
        String email = principal.getAttribute("email");
        if (email == null || email.isEmpty()) {
            throw new InvalidProfileDataException("Invalid OAuth2 principal: missing email");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidProfileDataException("User not found for email: " + email))
                .getId();
    }
}
