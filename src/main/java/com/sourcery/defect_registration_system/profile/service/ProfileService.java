package com.sourcery.defect_registration_system.profile.service;

import com.sourcery.defect_registration_system.office.enums.Country;
import com.sourcery.defect_registration_system.profile.dto.ProfileRequest;
import com.sourcery.defect_registration_system.profile.dto.ProfileResponse;
import com.sourcery.defect_registration_system.profile.entity.Profile;
import com.sourcery.defect_registration_system.profile.exceptions.InvalidProfileDataException;
import com.sourcery.defect_registration_system.profile.exceptions.UserNotFoundException;
import com.sourcery.defect_registration_system.profile.repository.ProfileRepository;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ProfileResponse getMyProfile(org.springframework.security.oauth2.core.user.OAuth2User principal) {
        UUID userId = authService.getCurrentUserId(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        Profile profile = profileRepository.findProfileByUserId(userId)
                .orElseGet(() -> Profile.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        Country countryEnum = convertToCountryEnum(profile.getCountry());
        return ProfileResponse.from(user, profile, countryEnum);
    }

    @Transactional
    public ProfileResponse updateMyProfile(org.springframework.security.oauth2.core.user.OAuth2User principal, ProfileRequest request) {
        UUID userId = authService.getCurrentUserId(principal);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
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

        Country countryEnum = convertToCountryEnum(profile.getCountry());
        return ProfileResponse.from(user, profile, countryEnum);
    }

    // Helper method to safely convert String to Country enum
    private Country convertToCountryEnum(String country) {
        if (country == null) return null;
        try {
            return Country.valueOf(country.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid country value: {}", country);
            return null;
        }
    }
}
