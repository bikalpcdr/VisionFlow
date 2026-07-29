package com.visionflow.core.patient.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.patient.dto.request.AssignDoctorRequest;
import com.visionflow.core.patient.dto.request.CreatePatientRequest;
import com.visionflow.core.patient.dto.request.UpdatePatientRequest;
import com.visionflow.core.patient.dto.response.PatientResponse;
import com.visionflow.core.patient.enums.Gender;
import com.visionflow.core.patient.service.PatientService;
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
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient profile management")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.REGISTER)
    @Operation(summary = "Create a patient profile for an existing PATIENT-role user")
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        return patientService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#id, authentication)")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Update patient profile")
    public PatientResponse update(@PathVariable Long id, @Valid @RequestBody UpdatePatientRequest request) {
        return patientService.update(id, request);
    }

    @PatchMapping("/{id}/assign-doctor")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Assign a doctor to a patient")
    public PatientResponse assignDoctor(@PathVariable Long id, @Valid @RequestBody AssignDoctorRequest request) {
        return patientService.assignDoctor(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#id, authentication)")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get patient by ID")
    public PatientResponse getById(@PathVariable Long id) {
        return patientService.getById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or #userId == authentication.principal.id")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get patient profile by user ID")
    public PatientResponse getByUserId(@PathVariable Long userId) {
        return patientService.getByUserId(userId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all patients with optional filters")
    public Page<PatientResponse> getAll(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return patientService.getAll(
                doctorId,
                gender != null ? gender.name() : null,
                active,
                pageable);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @SuccessMessage(entity = MessageConstant.PATIENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Deactivate a patient profile")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        patientService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
