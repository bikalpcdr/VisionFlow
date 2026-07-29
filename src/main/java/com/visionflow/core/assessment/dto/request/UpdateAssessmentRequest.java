package com.visionflow.core.assessment.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.enums.EyeConditionSeverity;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UpdateAssessmentRequest(

        @PastOrPresent(message = "Assessment date cannot be in the future")
        LocalDateTime assessmentDate,

        AssessmentStatus status,

        @Size(max = 20)
        String visualAcuityLeft,

        @Size(max = 20)
        String visualAcuityRight,

        @Size(max = 20)
        String visualAcuityBinocular,

        @DecimalMin(value = "-30.0") @DecimalMax(value = "30.0")
        Double sphereLeft,

        @DecimalMin(value = "-30.0") @DecimalMax(value = "30.0")
        Double sphereRight,

        @DecimalMin(value = "-10.0") @DecimalMax(value = "10.0")
        Double cylinderLeft,

        @DecimalMin(value = "-10.0") @DecimalMax(value = "10.0")
        Double cylinderRight,

        @Min(0) @Max(180)
        Integer axisLeft,

        @Min(0) @Max(180)
        Integer axisRight,

        String coverTestResult,

        @Size(max = 100)
        String npcResult,

        @Size(max = 100)
        String stereopsisResult,

        EyeConditionSeverity severity,

        @Size(max = 20)
        String affectedEye,

        @DecimalMin(value = "0.0")
        Double iopLeft,

        @DecimalMin(value = "0.0")
        Double iopRight,

        String chiefComplaint,
        String clinicalFindings,
        String recommendations,
        LocalDateTime followUpDate
) {
}
