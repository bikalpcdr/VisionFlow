package com.visionflow.core.auth.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.enums.Role;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    Long expiresIn,
    Long id,
    String firstName,
    String lastName,
    String email,
    Role role
) {}
