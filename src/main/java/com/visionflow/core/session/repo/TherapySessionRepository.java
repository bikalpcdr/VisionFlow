package com.visionflow.core.session.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.entity.TherapySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TherapySessionRepository extends JpaRepository<TherapySession, Long> {

    @Query("SELECT s FROM TherapySession s LEFT JOIN FETCH s.exerciseLogs WHERE s.id = :id AND s.deleted = false")
    Optional<TherapySession> findByIdWithLogs(@Param("id") Long id);

    @Query("SELECT COALESCE(MAX(s.sessionNumber), 0) FROM TherapySession s WHERE s.therapyPlan.id = :planId AND s.deleted = false")
    int findMaxSessionNumberByPlanId(@Param("planId") Long planId);

    boolean existsByIdAndDeletedFalse(Long id);
}
