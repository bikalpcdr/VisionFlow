package com.visionflow.core.therapyplan.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTherapyPlanRequest(

        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description,

        TherapyPlanStatus status,

        @Future(message = "End date must be in the future")
        LocalDate endDate,

        String goals,
        String notes
) {
}
