package com.sourcery.defect_registration_system.config.security;

import com.sourcery.defect_registration_system.identity.user.Role;
import com.sourcery.defect_registration_system.identity.user.User;
import com.sourcery.defect_registration_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomOidUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService;
    private final UserRepository userRepository;

    public CustomOidUserService(UserRepository userRepository) {
        this.oidcUserService = new OidcUserService();
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OidcUser oidcUser = this.oidcUserService.loadUser(userRequest);

        final String name = oidcUser.getAttribute("name");
        final String email = oidcUser.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build()));

        var authorities = Set.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
