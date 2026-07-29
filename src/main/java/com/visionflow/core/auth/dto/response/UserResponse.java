package com.visionflow.core.auth.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.enums.Role;
import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    Role role,
    boolean active,
    LocalDateTime createdAt
) {}
