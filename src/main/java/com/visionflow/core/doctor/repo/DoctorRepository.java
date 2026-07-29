package com.visionflow.core.doctor.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByIdAndDeletedFalse(Long id);

    Optional<Doctor> findByUserIdAndDeletedFalse(Long userId);

    boolean existsByLicenseNumberAndDeletedFalse(String licenseNumber);

    boolean existsByUserIdAndDeletedFalse(Long userId);
}
