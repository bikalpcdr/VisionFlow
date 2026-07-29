package com.visionflow.core.assessment.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.enums.AssessmentType;
import com.visionflow.core.assessment.enums.EyeConditionSeverity;

import java.time.LocalDateTime;

public record AssessmentResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        AssessmentType assessmentType,
        AssessmentStatus status,
        LocalDateTime assessmentDate,

        // Visual Acuity
        String visualAcuityLeft,
        String visualAcuityRight,
        String visualAcuityBinocular,

        // Refraction
        Double sphereLeft,
        Double sphereRight,
        Double cylinderLeft,
        Double cylinderRight,
        Integer axisLeft,
        Integer axisRight,

        // Binocular Vision
        String coverTestResult,
        String npcResult,
        String stereopsisResult,

        // Condition-Specific
        EyeConditionSeverity severity,
        String affectedEye,
        Double iopLeft,
        Double iopRight,

        // Clinical Notes
        String chiefComplaint,
        String clinicalFindings,
        String recommendations,
        LocalDateTime followUpDate,
        LocalDateTime createdAt
) {
}
