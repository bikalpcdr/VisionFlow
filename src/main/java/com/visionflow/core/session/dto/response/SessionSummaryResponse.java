package com.visionflow.core.session.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.PerformanceRating;
import com.visionflow.core.session.enums.SessionStatus;

import java.time.LocalDateTime;

public record SessionSummaryResponse(
        Long id,
        Long therapyPlanId,
        String therapyPlanTitle,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        Integer sessionNumber,
        LocalDateTime sessionDate,
        Integer durationMinutes,
        SessionStatus status,
        PerformanceRating overallPerformance,
        Integer totalExercises,
        Integer completedExercises,
        LocalDateTime createdAt
) {
}
