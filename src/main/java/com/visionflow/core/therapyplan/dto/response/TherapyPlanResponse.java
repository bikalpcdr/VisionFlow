package com.visionflow.core.therapyplan.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TherapyPlanResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        Long assessmentId,
        String title,
        String description,
        TherapyPlanStatus status,
        LocalDate startDate,
        LocalDate endDate,
        String goals,
        String notes,
        List<ExerciseResponse> exercises,
        LocalDateTime createdAt
) {
}
