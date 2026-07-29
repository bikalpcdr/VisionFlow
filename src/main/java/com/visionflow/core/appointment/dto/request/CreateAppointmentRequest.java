package com.visionflow.core.appointment.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(

        @NotNull Long patientId,

        @NotNull Long doctorId,

        Long assessmentId,

        Long therapyPlanId,

        @NotNull AppointmentType appointmentType,

        @NotNull @Future LocalDateTime appointmentDate,

        @NotNull @Positive Integer durationMinutes,

        String patientNotes
) {
}
