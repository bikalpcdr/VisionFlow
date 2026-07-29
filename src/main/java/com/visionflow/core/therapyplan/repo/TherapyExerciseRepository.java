package com.visionflow.core.therapyplan.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.entity.TherapyExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TherapyExerciseRepository extends JpaRepository<TherapyExercise, Long> {

    Optional<TherapyExercise> findByIdAndTherapyPlanIdAndDeletedFalse(Long id, Long therapyPlanId);
}
