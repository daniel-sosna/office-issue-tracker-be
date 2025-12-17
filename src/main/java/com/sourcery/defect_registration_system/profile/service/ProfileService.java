package com.sourcery.defect_registration_system.profile.service;

import com.sourcery.defect_registration_system.profile.dto.ProfileRequest;
import com.sourcery.defect_registration_system.profile.dto.ProfileResponse;
import com.sourcery.defect_registration_system.profile.entity.Profile;
import com.sourcery.defect_registration_system.profile.exceptions.InvalidProfileDataException;
import com.sourcery.defect_registration_system.profile.repository.ProfileRepository;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ProfileResponse getMyProfile(org.springframework.security.oauth2.core.user.OAuth2User principal) {
        UUID userId = authService.getCurrentUserId(principal);

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
    public ProfileResponse updateMyProfile(org.springframework.security.oauth2.core.user.OAuth2User principal, ProfileRequest request) {
        UUID userId = authService.getCurrentUserId(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidProfileDataException("User not found for id: " + userId));

        if (request.name() != null && !request.name().isBlank()) {
            userRepository.updateUserName(userId, request.name());
            user.setName(request.name());
        }

        Profile profile = profileRepository.findProfileByUserId(userId)
                .orElseGet(() -> Profile.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        profile.setDepartment(request.department());
        profile.setRole(request.role());
        profile.setStreetAddress(request.streetAddress());
        profile.setCity(request.city());
        profile.setStateProvince(request.stateProvince());
        profile.setPostcode(request.postcode());

        if (request.country() != null) {
            profile.setCountry(request.country().toUpperCase());
        }

        if (profileRepository.existsByUserId(userId)) {
            profileRepository.updateProfile(profile);
        } else {
            profileRepository.insertProfile(profile);
        }

        return ProfileResponse.from(user, profile);
    }

}
