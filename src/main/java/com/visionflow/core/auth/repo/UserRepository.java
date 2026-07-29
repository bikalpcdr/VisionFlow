package com.visionflow.core.auth.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Write-side repository.
 * Complex read queries (lists, projections, filters) go through MyBatis XML mappers.
 * Only persistence-critical lookups live here.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndDeletedFalse(String email);
}
