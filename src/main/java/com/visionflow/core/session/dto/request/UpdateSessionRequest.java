package com.visionflow.core.session.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.PerformanceRating;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateSessionRequest(

        LocalDateTime sessionDate,

        @Positive Integer durationMinutes,

        PerformanceRating overallPerformance,

        String sessionNotes,

        String doctorObservations,

        String patientFeedback
) {
}
