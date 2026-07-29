package com.visionflow.core.therapyplan.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TherapyPlanSummaryResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String title,
        TherapyPlanStatus status,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalExercises,
        LocalDateTime createdAt
) {
}
