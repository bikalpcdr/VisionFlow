package com.visionflow.core.assessment.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.entity.Assessment;
import com.visionflow.core.assessment.enums.AssessmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    Optional<Assessment> findByIdAndDeletedFalse(Long id);

    boolean existsByIdAndStatusAndDeletedFalse(Long id, AssessmentStatus status);
}
