package com.visionflow.core.auth.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.dto.request.LoginRequest;
import com.visionflow.core.auth.dto.request.RefreshTokenRequest;
import com.visionflow.core.auth.dto.request.RegisterRequest;
import com.visionflow.core.auth.dto.response.AuthResponse;
import com.visionflow.core.auth.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    UserResponse getById(Long id);

    Page<UserResponse> getAll(String role, Boolean active, Pageable pageable);

    void deactivate(Long id);
}
