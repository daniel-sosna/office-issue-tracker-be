package com.sourcery.defect_registration_system.user.config.security;

import com.sourcery.defect_registration_system.user.service.CustomOidcUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOidcUserService customOidUserService;
    private final String frontendUrl;

    public SecurityConfig(CustomOidcUserService customOidUserService,
                          @Value("${frontend.url}") String frontendUrl) {
        this.customOidUserService = customOidUserService;
        this.frontendUrl = frontendUrl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(req -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin(frontendUrl);
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization",  "X-XSRF-TOKEN"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
                    return configuration;
                }))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/logout")
                        .ignoringRequestMatchers("/issues/**")
                        .ignoringRequestMatchers("/offices/**")
                        .ignoringRequestMatchers("/api/profile/**")
                        .ignoringRequestMatchers("/api/notifications/**")
                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers( "/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/issues/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/offices").hasRole("ADMIN")
                        .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/notifications/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidUserService))
                        .defaultSuccessUrl(frontendUrl + "/", true))
                .logout(logout -> logout
                        .logoutSuccessUrl(frontendUrl + "/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN)));
        return http.build();
    }
}
