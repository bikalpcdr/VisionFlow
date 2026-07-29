package com.visionflow.core.report.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

public record PatientProgressReport(
        Long patientId,
        String patientName,
        Long activePlans,
        Long completedPlans,
        Long totalSessions,
        Long completedSessions,
        Long cancelledSessions,
        Long totalExercisesLogged,
        Long completedExercises,
        Double completionRate,
        String avgPerformance
) {
}
