package com.visionflow.core.assessment.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.enums.AssessmentType;
import com.visionflow.core.assessment.enums.EyeConditionSeverity;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateAssessmentRequest(

        @NotNull(message = "Patient ID is required")
        Long patientId,

        @NotNull(message = "Doctor ID is required")
        Long doctorId,

        @NotNull(message = "Assessment type is required")
        AssessmentType assessmentType,

        @NotNull(message = "Assessment date is required")
        @PastOrPresent(message = "Assessment date cannot be in the future")
        LocalDateTime assessmentDate,

        // Visual Acuity
        @Size(max = 20, message = "Visual acuity value must not exceed 20 characters")
        String visualAcuityLeft,

        @Size(max = 20, message = "Visual acuity value must not exceed 20 characters")
        String visualAcuityRight,

        @Size(max = 20, message = "Visual acuity value must not exceed 20 characters")
        String visualAcuityBinocular,

        // Refraction
        @DecimalMin(value = "-30.0", message = "Sphere must be between -30 and +30")
        @DecimalMax(value = "30.0", message = "Sphere must be between -30 and +30")
        Double sphereLeft,

        @DecimalMin(value = "-30.0", message = "Sphere must be between -30 and +30")
        @DecimalMax(value = "30.0", message = "Sphere must be between -30 and +30")
        Double sphereRight,

        @DecimalMin(value = "-10.0", message = "Cylinder must be between -10 and +10")
        @DecimalMax(value = "10.0", message = "Cylinder must be between -10 and +10")
        Double cylinderLeft,

        @DecimalMin(value = "-10.0", message = "Cylinder must be between -10 and +10")
        @DecimalMax(value = "10.0", message = "Cylinder must be between -10 and +10")
        Double cylinderRight,

        @Min(value = 0, message = "Axis must be between 0 and 180")
        @Max(value = 180, message = "Axis must be between 0 and 180")
        Integer axisLeft,

        @Min(value = 0, message = "Axis must be between 0 and 180")
        @Max(value = 180, message = "Axis must be between 0 and 180")
        Integer axisRight,

        // Binocular Vision
        String coverTestResult,

        @Size(max = 100, message = "NPC result must not exceed 100 characters")
        String npcResult,

        @Size(max = 100, message = "Stereopsis result must not exceed 100 characters")
        String stereopsisResult,

        // Condition-Specific
        EyeConditionSeverity severity,

        @Size(max = 20, message = "Affected eye must not exceed 20 characters")
        String affectedEye,

        @DecimalMin(value = "0.0", message = "IOP must be a positive value")
        Double iopLeft,

        @DecimalMin(value = "0.0", message = "IOP must be a positive value")
        Double iopRight,

        // Clinical Notes
        String chiefComplaint,
        String clinicalFindings,
        String recommendations,
        LocalDateTime followUpDate
) {
}
