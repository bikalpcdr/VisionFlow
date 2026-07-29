package com.visionflow.core.session.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.CompletionStatus;

public record ExerciseLogResponse(
        Long id,
        Long exerciseId,
        String exerciseName,
        String exerciseType,
        CompletionStatus completionStatus,
        Integer actualDurationMinutes,
        Integer difficultyRating,
        String patientNotes,
        String doctorNotes
) {
}
