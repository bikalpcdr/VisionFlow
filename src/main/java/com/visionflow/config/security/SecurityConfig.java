package com.visionflow.config.security;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String DOCTOR_ROLE = "DOCTOR";
    private static final String PATIENT_ROLE = "PATIENT";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers("/users/**").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/doctors/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers("/doctors/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers(HttpMethod.GET, "/patients/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .requestMatchers("/patients/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers(HttpMethod.GET, "/assessments/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .requestMatchers("/assessments/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers(HttpMethod.GET, "/therapy-plans/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .requestMatchers("/therapy-plans/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers(HttpMethod.GET, "/sessions/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .requestMatchers("/sessions/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE)
                        .requestMatchers(HttpMethod.GET, "/appointments/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .requestMatchers("/appointments/**").hasAnyRole(ADMIN_ROLE, DOCTOR_ROLE, PATIENT_ROLE)
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
