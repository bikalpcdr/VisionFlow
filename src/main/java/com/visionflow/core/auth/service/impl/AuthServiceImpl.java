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
import com.visionflow.core.auth.dto.response.UserResponse;
import com.visionflow.core.auth.entity.User;
import com.visionflow.core.auth.mapper.UserMapper;
import com.visionflow.core.auth.repo.UserReadMapper;
import com.visionflow.core.auth.repo.UserRepository;
import com.visionflow.core.auth.service.AuthService;
import com.visionflow.exception.GenericUncheckedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserReadMapper userReadMapper,
            UserMapper userMapper,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userReadMapper = userReadMapper;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedFalse(request.email())) {
            throw new GenericUncheckedException("Email already registered", "EMAIL_TAKEN", HttpStatus.CONFLICT.value());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User saved = userRepository.save(user);

        log.info("New user registered: id={}, role={}", saved.getId(), saved.getRole());
        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmailAndDeletedFalse(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

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

        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return buildAuthResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(String role, Boolean active, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        List<UserResponse> content = userReadMapper.findAll(role, active, offset, limit);
        long total = userReadMapper.countAll(role, active);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated: id={}", id);
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
