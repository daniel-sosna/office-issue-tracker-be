package com.sourcery.defect_registration_system.user.service;

import com.sourcery.defect_registration_system.exception.NotFoundException;
import com.sourcery.defect_registration_system.user.dto.UserSummaryDto;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with " + id + " id not found"));

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getImageUrl()
        );
    }

    public List<UserSummaryDto> getUsersForDropdown() {
        List<UserSummaryDto> allUsers = userRepository.findUsersForDropdown();
        return userRepository.findUsersForDropdown();
    }
}
