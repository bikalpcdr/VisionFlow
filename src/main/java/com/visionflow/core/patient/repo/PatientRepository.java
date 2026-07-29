package com.visionflow.core.patient.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByIdAndDeletedFalse(Long id);

    Optional<Patient> findByUserIdAndDeletedFalse(Long userId);

    boolean existsByUserIdAndDeletedFalse(Long userId);
}
