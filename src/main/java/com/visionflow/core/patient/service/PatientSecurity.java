package com.visionflow.core.patient.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.config.security.UserPrincipal;
import com.visionflow.core.patient.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Used in @PreAuthorize SpEL expressions as @patientSecurity.
 * Keeps authorization logic out of the controller and service layers.
 */
@Component("patientSecurity")
@RequiredArgsConstructor
public class PatientSecurity {

    private final PatientRepository patientRepository;

    public boolean isOwner(Long patientId, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        return patientRepository.findByIdAndDeletedFalse(patientId)
                .map(patient -> patient.getUser().getId().equals(principal.getId()))
                .orElse(false);
    }

    public boolean isAssignedDoctor(Long patientId, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        return patientRepository.findByIdAndDeletedFalse(patientId)
                .map(patient -> patient.getAssignedDoctor() != null &&
                        patient.getAssignedDoctor().getUser().getId().equals(principal.getId()))
                .orElse(false);
    }
}
