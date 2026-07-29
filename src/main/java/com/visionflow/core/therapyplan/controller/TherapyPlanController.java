package com.visionflow.core.therapyplan.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.therapyplan.dto.request.CreateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.request.ExerciseRequest;
import com.visionflow.core.therapyplan.dto.request.UpdateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanResponse;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanSummaryResponse;
import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import com.visionflow.core.therapyplan.service.TherapyPlanService;
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
@RequestMapping("/therapy-plans")
@RequiredArgsConstructor
@Tag(name = "Therapy Plans", description = "Vision therapy plan and exercise management")
@SecurityRequirement(name = "bearerAuth")
public class TherapyPlanController {

    private final TherapyPlanService therapyPlanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.REGISTER)
    @Operation(summary = "Create a new therapy plan with exercises")
    public TherapyPlanResponse create(@Valid @RequestBody CreateTherapyPlanRequest request) {
        return therapyPlanService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.UPDATED)
    @Operation(summary = "Update therapy plan details and status")
    public TherapyPlanResponse update(@PathVariable Long id,
                                      @Valid @RequestBody UpdateTherapyPlanRequest request) {
        return therapyPlanService.update(id, request);
    }

    @PostMapping("/{planId}/exercises")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.UPDATED)
    @Operation(summary = "Add an exercise to an existing therapy plan")
    public TherapyPlanResponse addExercise(@PathVariable Long planId,
                                           @Valid @RequestBody ExerciseRequest request) {
        return therapyPlanService.addExercise(planId, request);
    }

    @DeleteMapping("/{planId}/exercises/{exerciseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.UPDATED)
    @Operation(summary = "Remove an exercise from a therapy plan")
    public TherapyPlanResponse removeExercise(@PathVariable Long planId,
                                              @PathVariable Long exerciseId) {
        return therapyPlanService.removeExercise(planId, exerciseId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#id, authentication)")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.FETCHED)
    @Operation(summary = "Get therapy plan by ID with full exercise list")
    public TherapyPlanResponse getById(@PathVariable Long id) {
        return therapyPlanService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all therapy plans with optional filters")
    public Page<TherapyPlanSummaryResponse> getAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) TherapyPlanStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return therapyPlanService.getAll(
                patientId, doctorId,
                status != null ? status.name() : null,
                pageable);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#patientId, authentication)")
    @SuccessMessage(entity = MessageConstant.THERAPY_PLAN, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all therapy plans for a specific patient")
    public List<TherapyPlanSummaryResponse> getByPatientId(@PathVariable Long patientId) {
        return therapyPlanService.getByPatientId(patientId);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(summary = "Cancel a therapy plan")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        therapyPlanService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
