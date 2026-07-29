package com.visionflow.core.therapyplan.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.entity.TherapyPlan;
import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TherapyPlanRepository extends JpaRepository<TherapyPlan, Long> {

    @Query("SELECT tp FROM TherapyPlan tp LEFT JOIN FETCH tp.exercises WHERE tp.id = :id AND tp.deleted = false")
    Optional<TherapyPlan> findByIdWithExercises(@Param("id") Long id);

    Optional<TherapyPlan> findByIdAndDeletedFalse(Long id);

    boolean existsByPatientIdAndStatusAndDeletedFalse(Long patientId, TherapyPlanStatus status);
}
