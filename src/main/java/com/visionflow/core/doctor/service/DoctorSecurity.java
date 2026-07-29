package com.visionflow.core.doctor.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.config.security.UserPrincipal;
import com.visionflow.core.doctor.repo.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Used in @PreAuthorize SpEL expressions as @doctorSecurity.
 * Keeps authorization logic out of the controller and service layers.
 */
@Component("doctorSecurity")
@RequiredArgsConstructor
public class DoctorSecurity {

    private final DoctorRepository doctorRepository;

    public boolean isOwner(Long doctorId, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        return doctorRepository.findByIdAndDeletedFalse(doctorId)
                .map(doctor -> doctor.getUser().getId().equals(principal.getId()))
                .orElse(false);
    }
}
