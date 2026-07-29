package com.visionflow.core.therapyplan.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.ExerciseType;
import com.visionflow.core.therapyplan.enums.FrequencyUnit;

public record ExerciseResponse(
        Long id,
        ExerciseType exerciseType,
        String name,
        String description,
        Integer frequency,
        FrequencyUnit frequencyUnit,
        Integer durationMinutes,
        Integer repetitions,
        Integer orderIndex,
        String instructions
) {
}
