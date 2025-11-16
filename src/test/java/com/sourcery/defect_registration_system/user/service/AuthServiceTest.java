package com.sourcery.defect_registration_system.user.service;

import com.sourcery.defect_registration_system.user.common.Role;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.exception.BadRequestException;
import com.sourcery.defect_registration_system.user.exception.NotFoundException;
import com.sourcery.defect_registration_system.user.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User principal;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "defaultPicture", "http://default/pic.png");
    }

    @Test
    void getCurrentUserInfo_whenPrincipalIsNull_shouldThrowUnauthorizedException() {
        assertThatThrownBy(() -> authService.getCurrentUserInfo(null))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Authentication is required.");
    }

    @Test
    void getCurrentUserInfo_whenEmailMissing_shouldThrowBadRequestException() {
        when(principal.getAttribute("email")).thenReturn(null);

        assertThatThrownBy(() -> authService.getCurrentUserInfo(principal))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email attribute is missing from OAuth2 principal.");
    }

    @Test
    void getCurrentUserInfo_whenUserNotFound_shouldThrowNotFoundException() {
        String email = "test@example.com";
        when(principal.getAttribute("email")).thenReturn(email);
        when(principal.getAttribute("picture")).thenReturn("http://image/pic.png");
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getCurrentUserInfo(principal))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found by email: " + email);
    }

    @Test
    void getCurrentUserInfo_whenUserFoundWithPicture_shouldReturnUserInfoMap() {
        String email = "test@example.com";
        String picture = "http://image/pic.png";

        User user = new User();
        user.setEmail(email);
        user.setName("Test User");
        user.setRole(Role.USER);

        when(principal.getAttribute("email")).thenReturn(email);
        when(principal.getAttribute("picture")).thenReturn(picture);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Map<String, Object> result = authService.getCurrentUserInfo(principal);

        assertThat(result).isNotNull();
        assertThat(result.get("email")).isEqualTo(email);
        assertThat(result.get("name")).isEqualTo("Test User");
        assertThat(result.get("role")).isEqualTo(Role.USER);
        assertThat(result.get("picture")).isEqualTo(picture);
    }

    @Test
    void getCurrentUserInfo_whenPictureMissing_shouldUseDefaultPicture() {
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);
        user.setName("Test User");
        user.setRole(Role.USER);

        when(principal.getAttribute("email")).thenReturn(email);
        when(principal.getAttribute("picture")).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Map<String, Object> result = authService.getCurrentUserInfo(principal);

        assertThat(result).isNotNull();
        assertThat(result.get("email")).isEqualTo(email);
        assertThat(result.get("name")).isEqualTo("Test User");
        assertThat(result.get("role")).isEqualTo(Role.USER);
        assertThat(result.get("picture")).isEqualTo("http://default/pic.png");
    }
}