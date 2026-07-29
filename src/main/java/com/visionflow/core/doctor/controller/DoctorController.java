package com.visionflow.core.doctor.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.doctor.dto.request.CreateDoctorRequest;
import com.visionflow.core.doctor.dto.request.UpdateDoctorRequest;
import com.visionflow.core.doctor.dto.response.DoctorResponse;
import com.visionflow.core.doctor.enums.Specialization;
import com.visionflow.core.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor profile management")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {

    private static final String DOCTOR = "Doctor";

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.REGISTER)
    @Operation(summary = "Create a doctor profile for an existing DOCTOR-role user")
    public DoctorResponse create(@Valid @RequestBody CreateDoctorRequest request) {
        return doctorService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorSecurity.isOwner(#id, authentication))")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.UPDATED)
    @Operation(summary = "Update doctor profile")
    public DoctorResponse update(@PathVariable Long id, @Valid @RequestBody UpdateDoctorRequest request) {
        return doctorService.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.FETCHED)
    @Operation(summary = "Get doctor by ID")
    public DoctorResponse getById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.FETCHED)
    @Operation(summary = "Get doctor profile by user ID")
    public DoctorResponse getByUserId(@PathVariable Long userId) {
        return doctorService.getByUserId(userId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all doctors with optional filters")
    public Page<DoctorResponse> getAll(
            @RequestParam(required = false) Specialization specialization,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return doctorService.getAll(
                specialization != null ? specialization.name() : null, active, pageable);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @SuccessMessage(entity = DOCTOR, action = MessageConstant.UPDATED)
    @Operation(summary = "Deactivate a doctor profile")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        doctorService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
