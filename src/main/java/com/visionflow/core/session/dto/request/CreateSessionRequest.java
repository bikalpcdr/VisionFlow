package com.visionflow.core.session.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public record CreateSessionRequest(

        @NotNull Long therapyPlanId,

        @NotNull LocalDateTime sessionDate,

        @Positive Integer durationMinutes,

        String sessionNotes,

        String doctorObservations,

        @Valid List<ExerciseLogRequest> exerciseLogs
) {
}
