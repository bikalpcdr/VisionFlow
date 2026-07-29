package com.visionflow.core.auth.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.auth.dto.request.LoginRequest;
import com.visionflow.core.auth.dto.request.RefreshTokenRequest;
import com.visionflow.core.auth.dto.request.RegisterRequest;
import com.visionflow.core.auth.dto.response.AuthResponse;
import com.visionflow.core.auth.dto.response.UserResponse;
import com.visionflow.core.auth.enums.Role;
import com.visionflow.core.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, and token management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.REGISTER)
    @Operation(summary = "Register a new user")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.LOGIN)
    @Operation(summary = "Login and receive JWT tokens")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/auth/refresh")
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.REFRESH)
    @Operation(summary = "Refresh access token using refresh token")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or #id == authentication.principal.id")
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.FETCHED)
    @Operation(summary = "Get user by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public UserResponse getById(@PathVariable Long id) {
        return authService.getById(id);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all users with optional filters", security = @SecurityRequirement(name = "bearerAuth"))
    public Page<UserResponse> getAll(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return authService.getAll(role != null ? role.name() : null, active, pageable);
    }

    @PatchMapping("/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @SuccessMessage(entity = MessageConstant.USER, action = MessageConstant.UPDATED)
    @Operation(summary = "Deactivate a user account", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        authService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
