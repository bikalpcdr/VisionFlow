package com.visionflow.core.patient.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import jakarta.validation.constraints.NotNull;

public record AssignDoctorRequest(

        @NotNull(message = "Doctor ID is required")
        Long doctorId
) {
}
