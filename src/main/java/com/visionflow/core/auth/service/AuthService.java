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

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);
}
