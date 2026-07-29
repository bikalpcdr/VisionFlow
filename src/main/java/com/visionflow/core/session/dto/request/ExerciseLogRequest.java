package com.visionflow.core.session.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.CompletionStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExerciseLogRequest(

        @NotNull Long exerciseId,

        @NotNull CompletionStatus completionStatus,

        @Positive Integer actualDurationMinutes,

        @Min(1) @Max(5) Integer difficultyRating,

        String patientNotes,

        String doctorNotes
) {
}
