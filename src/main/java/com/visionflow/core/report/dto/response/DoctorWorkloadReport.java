package com.visionflow.core.report.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

public record DoctorWorkloadReport(
        Long doctorId,
        String doctorName,
        String specialization,
        Long totalAppointments,
        Long completedAppointments,
        Long cancelledAppointments,
        Long noShowAppointments,
        Long totalSessions,
        Long completedSessions,
        Long totalAssessments,
        Long totalActivePlans
) {
}
