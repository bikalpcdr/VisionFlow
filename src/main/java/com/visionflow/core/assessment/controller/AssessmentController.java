package com.visionflow.core.assessment.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.assessment.dto.request.CreateAssessmentRequest;
import com.visionflow.core.assessment.dto.request.UpdateAssessmentRequest;
import com.visionflow.core.assessment.dto.response.AssessmentResponse;
import com.visionflow.core.assessment.dto.response.AssessmentSummaryResponse;
import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.enums.AssessmentType;
import com.visionflow.core.assessment.service.AssessmentService;
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
@RequestMapping("/assessments")
@RequiredArgsConstructor
@Tag(name = "Assessments", description = "Vision assessment management")
@SecurityRequirement(name = "bearerAuth")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.ASSESSMENT, action = MessageConstant.REGISTER)
    @Operation(summary = "Create a new vision assessment")
    public AssessmentResponse create(@Valid @RequestBody CreateAssessmentRequest request) {
        return assessmentService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.ASSESSMENT, action = MessageConstant.UPDATED)
    @Operation(summary = "Update an assessment")
    public AssessmentResponse update(@PathVariable Long id,
                                     @Valid @RequestBody UpdateAssessmentRequest request) {
        return assessmentService.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.ASSESSMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get assessment by ID")
    public AssessmentResponse getById(@PathVariable Long id) {
        return assessmentService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.ASSESSMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all assessments with optional filters")
    public Page<AssessmentSummaryResponse> getAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) AssessmentType assessmentType,
            @RequestParam(required = false) AssessmentStatus status,
            @PageableDefault(size = 20, sort = "assessmentDate") Pageable pageable) {
        return assessmentService.getAll(
                patientId, doctorId,
                assessmentType != null ? assessmentType.name() : null,
                status != null ? status.name() : null,
                pageable);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @patientSecurity.isOwner(#patientId, authentication)")
    @SuccessMessage(entity = MessageConstant.ASSESSMENT, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all assessments for a specific patient")
    public List<AssessmentSummaryResponse> getByPatientId(@PathVariable Long patientId) {
        return assessmentService.getByPatientId(patientId);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(summary = "Cancel an assessment")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        assessmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
