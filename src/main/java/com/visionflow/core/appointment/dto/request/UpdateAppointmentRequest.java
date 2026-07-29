package com.visionflow.core.appointment.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateAppointmentRequest(

        AppointmentType appointmentType,

        @Future LocalDateTime appointmentDate,

        @Positive Integer durationMinutes,

        String patientNotes,

        String doctorNotes
) {
}
