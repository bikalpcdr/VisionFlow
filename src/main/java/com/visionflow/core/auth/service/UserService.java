package com.visionflow.core.auth.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.dto.response.UserResponse;
import com.visionflow.core.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User getEntityByEmail(String email);

    User getEntityById(Long id);

    UserResponse getById(Long id);

    Page<UserResponse> getAll(String role, Boolean active, Pageable pageable);

    boolean existsByEmail(String email);

    User save(User user);

    void deactivate(Long id);
}
