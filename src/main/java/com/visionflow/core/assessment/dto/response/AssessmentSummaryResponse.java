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

public record AssessmentSummaryResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        AssessmentType assessmentType,
        AssessmentStatus status,
        EyeConditionSeverity severity,
        LocalDateTime assessmentDate,
        LocalDateTime followUpDate,
        LocalDateTime createdAt
) {
}
