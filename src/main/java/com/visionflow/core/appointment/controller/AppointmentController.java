package com.visionflow.core.appointment.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.appointment.dto.request.CancelAppointmentRequest;
import com.visionflow.core.appointment.dto.request.CreateAppointmentRequest;
import com.visionflow.core.appointment.dto.request.UpdateAppointmentRequest;
import com.visionflow.core.appointment.dto.response.AppointmentResponse;
import com.visionflow.core.appointment.dto.response.AppointmentSummaryResponse;
import com.visionflow.core.appointment.enums.AppointmentStatus;
import com.visionflow.core.appointment.enums.AppointmentType;
import com.visionflow.core.appointment.service.AppointmentService;
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

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment scheduling and management")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.REGISTER)
    @Operation(summary = "Request a new appointment")
    public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest request) {
        return appointmentService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Update appointment details")
    public AppointmentResponse update(@PathVariable Long id,
                                      @Valid @RequestBody UpdateAppointmentRequest request) {
        return appointmentService.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get appointment by ID")
    public AppointmentResponse getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all appointments with optional filters")
    public Page<AppointmentSummaryResponse> getAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) AppointmentType appointmentType,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable) {
        return appointmentService.getAll(
                patientId, doctorId,
                status != null ? status.name() : null,
                appointmentType != null ? appointmentType.name() : null,
                pageable);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#patientId, authentication)")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all appointments for a patient")
    public List<AppointmentSummaryResponse> getByPatientId(@PathVariable Long patientId) {
        return appointmentService.getByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all appointments for a doctor")
    public List<AppointmentSummaryResponse> getByDoctorId(@PathVariable Long doctorId) {
        return appointmentService.getByDoctorId(doctorId);
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Confirm a requested appointment")
    public AppointmentResponse confirm(@PathVariable Long id) {
        return appointmentService.confirm(id);
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Start a confirmed appointment")
    public AppointmentResponse start(@PathVariable Long id) {
        return appointmentService.start(id);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.APPOINTMENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Complete an in-progress appointment")
    public AppointmentResponse complete(@PathVariable Long id) {
        return appointmentService.complete(id);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<Void> cancel(@PathVariable Long id,
                                       @RequestBody(required = false) CancelAppointmentRequest request) {
        appointmentService.cancel(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/no-show")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(summary = "Mark appointment as no-show")
    public ResponseEntity<Void> noShow(@PathVariable Long id) {
        appointmentService.noShow(id);
        return ResponseEntity.noContent().build();
    }
}
