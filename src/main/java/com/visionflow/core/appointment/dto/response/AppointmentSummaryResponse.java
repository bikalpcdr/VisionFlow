package com.visionflow.core.appointment.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.enums.AppointmentStatus;
import com.visionflow.core.appointment.enums.AppointmentType;

import java.time.LocalDateTime;

public record AppointmentSummaryResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        AppointmentType appointmentType,
        AppointmentStatus status,
        LocalDateTime appointmentDate,
        Integer durationMinutes,
        LocalDateTime createdAt
) {
}
