package com.visionflow.core.auth.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.config.security.JwtService;
import com.visionflow.core.auth.dto.request.LoginRequest;
import com.visionflow.core.auth.dto.request.RefreshTokenRequest;
import com.visionflow.core.auth.dto.request.RegisterRequest;
import com.visionflow.core.auth.dto.response.AuthResponse;
import com.visionflow.core.auth.entity.User;
import com.visionflow.core.auth.mapper.UserMapper;
import com.visionflow.core.auth.service.AuthService;
import com.visionflow.core.auth.service.UserService;
import com.visionflow.exception.GenericUncheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new GenericUncheckedException("Email already registered", "EMAIL_TAKEN", HttpStatus.CONFLICT.value());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User saved = userService.save(user);

        log.info("New user registered: id={}, role={}", saved.getId(), saved.getRole());
        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (BadCredentialsException e) {
            throw new GenericUncheckedException("Invalid credentials. Please check your email or password!", "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED.value());
        }

        User user = userService.getEntityByEmail(request.email());

        log.info("User logged in: id={}, role={}", user.getId(), user.getRole());
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        String email;
        try {
            email = jwtService.extractSubject(request.refreshToken());
        } catch (Exception ex) {
            throw new GenericUncheckedException("Invalid refresh token", "INVALID_TOKEN", HttpStatus.UNAUTHORIZED.value());
        }

        if (!jwtService.isTokenValid(request.refreshToken(), email)) {
            throw new GenericUncheckedException("Refresh token expired or invalid", "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED.value());
        }

        User user = userService.getEntityByEmail(email);
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        Map<String, Object> claims = Map.of("role", user.getRole().name());
        String accessToken = jwtService.generateAccessToken(user.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpiration(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
