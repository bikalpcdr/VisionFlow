package com.visionflow.core.appointment.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

public record CancelAppointmentRequest(
        String cancellationReason
) {
}
