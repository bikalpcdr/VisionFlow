package com.visionflow.core.auth.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

    @NotBlank(message = "Refresh token is required")
    String refreshToken
) {}
