package com.visionflow.core.doctor.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.enums.Specialization;
import jakarta.validation.constraints.*;

public record CreateDoctorRequest(

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Specialization is required")
        Specialization specialization,

        @NotBlank(message = "License number is required")
        @Size(max = 100, message = "License number must not exceed 100 characters")
        String licenseNumber,

        @NotBlank(message = "Qualification is required")
        @Size(max = 255, message = "Qualification must not exceed 255 characters")
        String qualification,

        @NotNull(message = "Years of experience is required")
        @Min(value = 0, message = "Years of experience must be 0 or more")
        @Max(value = 60, message = "Years of experience must not exceed 60")
        Integer yearsOfExperience,

        @Size(max = 1000, message = "Bio must not exceed 1000 characters")
        String bio,

        @Size(max = 200, message = "Clinic name must not exceed 200 characters")
        String clinicName,

        @Size(max = 500, message = "Clinic address must not exceed 500 characters")
        String clinicAddress
) {
}
