package com.visionflow.core.therapyplan.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.ExerciseType;
import com.visionflow.core.therapyplan.enums.FrequencyUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExerciseRequest(

        @NotNull(message = "Exercise type is required")
        ExerciseType exerciseType,

        @NotBlank(message = "Exercise name is required")
        @Size(max = 200, message = "Exercise name must not exceed 200 characters")
        String name,

        String description,

        @NotNull(message = "Frequency is required")
        @Min(value = 1, message = "Frequency must be at least 1")
        Integer frequency,

        @NotNull(message = "Frequency unit is required")
        FrequencyUnit frequencyUnit,

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1 minute")
        Integer durationMinutes,

        @Min(value = 1, message = "Repetitions must be at least 1")
        Integer repetitions,

        @NotNull(message = "Order index is required")
        @Min(value = 1, message = "Order index must be at least 1")
        Integer orderIndex,

        String instructions
) {
}
