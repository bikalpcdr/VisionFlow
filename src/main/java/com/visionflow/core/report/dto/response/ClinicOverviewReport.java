package com.visionflow.core.report.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

public record ClinicOverviewReport(
        Long totalPatients,
        Long totalDoctors,
        Long totalAppointments,
        Long completedAppointments,
        Long cancelledAppointments,
        Long totalAssessments,
        Long completedAssessments,
        Long totalTherapyPlans,
        Long activeTherapyPlans,
        Long totalSessions,
        Long completedSessions
) {
}
