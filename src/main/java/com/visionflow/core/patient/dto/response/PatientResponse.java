package com.visionflow.core.patient.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.patient.enums.BloodGroup;
import com.visionflow.core.patient.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Integer age,
        Gender gender,
        BloodGroup bloodGroup,
        String address,
        String emergencyContactName,
        String emergencyContactPhone,
        String allergies,
        String medicalNotes,
        Long assignedDoctorId,
        String assignedDoctorName,
        boolean active,
        LocalDateTime createdAt
) {
}
