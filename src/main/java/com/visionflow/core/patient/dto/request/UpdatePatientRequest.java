package com.visionflow.core.patient.dto.request;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.patient.enums.BloodGroup;
import com.visionflow.core.patient.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdatePatientRequest(

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        Gender gender,

        BloodGroup bloodGroup,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        @Size(max = 150, message = "Emergency contact name must not exceed 150 characters")
        String emergencyContactName,

        @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
        String emergencyContactPhone,

        @Size(max = 500, message = "Allergies must not exceed 500 characters")
        String allergies,

        String medicalNotes
) {
}
