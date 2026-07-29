package com.visionflow.core.session.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.session.dto.request.CreateSessionRequest;
import com.visionflow.core.session.dto.request.UpdateSessionRequest;
import com.visionflow.core.session.dto.response.SessionResponse;
import com.visionflow.core.session.dto.response.SessionSummaryResponse;
import com.visionflow.core.session.enums.SessionStatus;
import com.visionflow.core.session.service.SessionService;
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
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Therapy session and progress tracking")
@SecurityRequirement(name = "bearerAuth")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.REGISTER)
    @Operation(summary = "Create a new therapy session")
    public SessionResponse create(@Valid @RequestBody CreateSessionRequest request) {
        return sessionService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.UPDATED)
    @Operation(summary = "Update a therapy session")
    public SessionResponse update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateSessionRequest request) {
        return sessionService.update(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.FETCHED)
    @Operation(summary = "Get session by ID with full exercise logs")
    public SessionResponse getById(@PathVariable Long id) {
        return sessionService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all sessions with optional filters")
    public Page<SessionSummaryResponse> getAll(
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) SessionStatus status,
            @PageableDefault(size = 20, sort = "sessionDate") Pageable pageable) {
        return sessionService.getAll(
                planId, patientId, doctorId,
                status != null ? status.name() : null,
                pageable);
    }

    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all sessions for a therapy plan")
    public List<SessionSummaryResponse> getByPlanId(@PathVariable Long planId) {
        return sessionService.getByPlanId(planId);
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.UPDATED)
    @Operation(summary = "Start a scheduled session")
    public SessionResponse start(@PathVariable Long id) {
        return sessionService.start(id);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @SuccessMessage(entity = MessageConstant.SESSION, action = MessageConstant.UPDATED)
    @Operation(summary = "Complete an in-progress session")
    public SessionResponse complete(@PathVariable Long id) {
        return sessionService.complete(id);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(summary = "Cancel a session")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        sessionService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
