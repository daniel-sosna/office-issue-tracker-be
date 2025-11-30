package com.sourcery.defect_registration_system.unit.user;

import com.sourcery.defect_registration_system.exception.NotFoundException;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.enums.Role;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import com.sourcery.defect_registration_system.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User buildUser(String name, String email, Role role, String imageUrl) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .role(role)
                .imageUrl(imageUrl)
                .build();
    }

    @Test
    void getUserById_whenOfficeExists_shouldReturnResponse() {
        User user = buildUser("name", "email", Role.USER, "url");
        UUID id = user.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(id);

        assertThat(result.name()).isEqualTo("name");
        assertThat(result.email()).isEqualTo("email");
        assertThat(result.role()).isEqualTo(Role.USER);
        assertThat(result.picture()).isEqualTo("url");
    }

    @Test
    void getUserById_whenOfficeNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(id));
    }
}

