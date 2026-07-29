package com.visionflow.core.appointment.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.enums.AppointmentStatus;
import com.visionflow.core.appointment.enums.AppointmentType;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        Long assessmentId,
        Long therapyPlanId,
        String therapyPlanTitle,
        AppointmentType appointmentType,
        AppointmentStatus status,
        LocalDateTime appointmentDate,
        Integer durationMinutes,
        String patientNotes,
        String doctorNotes,
        String cancellationReason,
        LocalDateTime createdAt
) {
}
