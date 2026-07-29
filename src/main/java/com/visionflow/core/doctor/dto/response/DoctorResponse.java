package com.visionflow.core.doctor.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.enums.Specialization;

import java.time.LocalDateTime;

public record DoctorResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        Specialization specialization,
        String licenseNumber,
        String qualification,
        Integer yearsOfExperience,
        String bio,
        String clinicName,
        String clinicAddress,
        boolean active,
        LocalDateTime createdAt
) {
}
