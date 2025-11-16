package com.sourcery.defect_registration_system.user.service;

import com.sourcery.defect_registration_system.user.common.Role;
import com.sourcery.defect_registration_system.user.entity.User;
import com.sourcery.defect_registration_system.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension .class)
class CustomOidcUserServiceTest {

    @Mock
    private OidcUserService oidcUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OidcUserRequest oidcUserRequest;

    @Mock
    private OidcUser oidcUser;

    @Mock
    private OidcIdToken idToken;

    @Mock
    private OidcUserInfo userInfo;

    @InjectMocks
    private CustomOidcUserService customOidcUserService;

    @Test
    void loadUser_whenUserExists_shouldNotInsertAndUseExistingRole() {
        String email = "existing@example.com";

        when(oidcUserService.loadUser(oidcUserRequest)).thenReturn(oidcUser);
        when(oidcUser.getAttribute("name")).thenReturn("Existing User");
        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getClaim("picture")).thenReturn("http://image/pic.png");
        when(oidcUser.getIdToken()).thenReturn(idToken);

        when(userInfo.getClaims()).thenReturn(Map.of(
                "sub", "123",
                "email", email
        ));

        User existingUser = User.builder()
                .id(UUID.randomUUID())
                .name("Existing User")
                .email(email)
                .imageUrl("http://old/pic.png")
                .role(com.sourcery.defect_registration_system.user.common.Role.ADMIN)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        OidcUser result = customOidcUserService.loadUser(oidcUserRequest);

        verify(userRepository, never()).insert(any(User.class));

        assertThat(result).isNotNull();
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ADMIN");

        assertThat(result.getIdToken()).isEqualTo(idToken);
        assertThat(result.getUserInfo()).isEqualTo(userInfo);
    }

    @Test
    void loadUser_whenUserDoesNotExist_shouldInsertNewUserWithUserInfoPicture() {
        String email = "new@example.com";
        String name = "New User";
        String pictureFromUserInfo = "http://userinfo/pic.png";

        when(oidcUserService.loadUser(oidcUserRequest)).thenReturn(oidcUser);
        when(oidcUser.getAttribute("name")).thenReturn(name);
        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getClaim("picture")).thenReturn(pictureFromUserInfo);
        when(oidcUser.getIdToken()).thenReturn(idToken);

        when(userInfo.getClaims()).thenReturn(Map.of(
                "sub", "123",
                "email", email
        ));

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(userRepository).insert(userCaptor.capture());

        OidcUser result = customOidcUserService.loadUser(oidcUserRequest);

        verify(userRepository).insert(any(User.class));

        User insertedUser = userCaptor.getValue();
        assertThat(insertedUser.getId()).isNotNull();
        assertThat(insertedUser.getName()).isEqualTo(name);
        assertThat(insertedUser.getEmail()).isEqualTo(email);
        assertThat(insertedUser.getImageUrl()).isEqualTo(pictureFromUserInfo);
        assertThat(insertedUser.getRole()).isEqualTo(Role.USER);

        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("USER");
    }

    @Test
    void loadUser_whenUserInfoIsNull_shouldUseAttributePicture() {
        String email = "attr@example.com";
        String name = "Attr User";
        String pictureFromAttr = "http://attr/pic.png";

        when(oidcUserService.loadUser(oidcUserRequest)).thenReturn(oidcUser);
        when(oidcUser.getAttribute("name")).thenReturn(name);
        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getUserInfo()).thenReturn(null); // nėra userInfo
        when(oidcUser.getAttribute("picture")).thenReturn(pictureFromAttr);
        when(oidcUser.getIdToken()).thenReturn(idToken);

        when(idToken.getClaims()).thenReturn(Map.of(
                "sub", "123",
                "email", email
        ));

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(userRepository).insert(userCaptor.capture());

        OidcUser result = customOidcUserService.loadUser(oidcUserRequest);

        verify(userRepository).insert(any(User.class));

        User insertedUser = userCaptor.getValue();
        assertThat(insertedUser.getImageUrl()).isEqualTo(pictureFromAttr);
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("USER");
    }
}