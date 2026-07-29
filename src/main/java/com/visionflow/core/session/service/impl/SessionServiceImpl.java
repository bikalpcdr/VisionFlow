package com.visionflow.core.session.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.dto.request.CreateSessionRequest;
import com.visionflow.core.session.dto.request.ExerciseLogRequest;
import com.visionflow.core.session.dto.request.UpdateSessionRequest;
import com.visionflow.core.session.dto.response.SessionResponse;
import com.visionflow.core.session.dto.response.SessionSummaryResponse;
import com.visionflow.core.session.entity.ExerciseLog;
import com.visionflow.core.session.entity.TherapySession;
import com.visionflow.core.session.enums.SessionStatus;
import com.visionflow.core.session.mapper.SessionMapper;
import com.visionflow.core.session.repo.SessionReadMapper;
import com.visionflow.core.session.repo.TherapySessionRepository;
import com.visionflow.core.session.service.SessionService;
import com.visionflow.core.therapyplan.entity.TherapyExercise;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import com.visionflow.core.therapyplan.repo.TherapyExerciseRepository;
import com.visionflow.core.therapyplan.service.TherapyPlanService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final TherapySessionRepository sessionRepository;
    private final SessionReadMapper sessionReadMapper;
    private final SessionMapper sessionMapper;
    private final TherapyPlanService therapyPlanService;
    private final TherapyExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public SessionResponse create(CreateSessionRequest request) {
        TherapyPlan plan = therapyPlanService.getEntityById(request.therapyPlanId());

        if (plan.getStatus() != TherapyPlanStatus.ACTIVE) {
            throw new IllegalStateException("Sessions can only be created for ACTIVE therapy plans.");
        }

        TherapySession session = new TherapySession();
        session.setTherapyPlan(plan);
        session.setPatient(plan.getPatient());
        session.setDoctor(plan.getDoctor());
        session.setSessionDate(request.sessionDate());
        session.setDurationMinutes(request.durationMinutes());
        session.setSessionNotes(request.sessionNotes());
        session.setDoctorObservations(request.doctorObservations());
        session.setStatus(SessionStatus.SCHEDULED);
        session.setSessionNumber(sessionRepository.findMaxSessionNumberByPlanId(plan.getId()) + 1);

        if (request.exerciseLogs() != null && !request.exerciseLogs().isEmpty()) {
            for (ExerciseLogRequest logReq : request.exerciseLogs()) {
                session.getExerciseLogs().add(buildExerciseLog(logReq, session));
            }
        }

        TherapySession saved = sessionRepository.save(session);
        return sessionReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found after save"));
    }

    @Override
    @Transactional
    public SessionResponse update(Long id, UpdateSessionRequest request) {
        TherapySession session = getEntityById(id);
        if (session.getStatus() == SessionStatus.COMPLETED || session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a " + session.getStatus() + " session.");
        }
        sessionMapper.updateSession(request, session);
        sessionRepository.save(session);
        return sessionReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }

    @Override
    @Transactional
    public SessionResponse start(Long id) {
        TherapySession session = getEntityById(id);
        session.setStatus(switch (session.getStatus()) {
            case SCHEDULED -> SessionStatus.IN_PROGRESS;
            case IN_PROGRESS -> throw new IllegalStateException("Session is already IN_PROGRESS.");
            case COMPLETED -> throw new IllegalStateException("Session is already COMPLETED.");
            case CANCELLED -> throw new IllegalStateException("Cannot start a CANCELLED session.");
        });
        sessionRepository.save(session);
        return sessionReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }

    @Override
    @Transactional
    public SessionResponse complete(Long id) {
        TherapySession session = getEntityById(id);
        session.setStatus(switch (session.getStatus()) {
            case IN_PROGRESS -> SessionStatus.COMPLETED;
            case SCHEDULED -> throw new IllegalStateException("Session must be IN_PROGRESS before completing.");
            case COMPLETED -> throw new IllegalStateException("Session is already COMPLETED.");
            case CANCELLED -> throw new IllegalStateException("Cannot complete a CANCELLED session.");
        });
        sessionRepository.save(session);
        return sessionReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        TherapySession session = getEntityById(id);
        if (session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a COMPLETED session.");
        }
        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Session is already CANCELLED.");
        }
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponse getById(Long id) {
        return sessionReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TherapySession getEntityById(Long id) {
        return sessionRepository.findByIdWithLogs(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionSummaryResponse> getAll(Long planId, Long patientId, Long doctorId, String status, Pageable pageable) {
        long total = sessionReadMapper.countAll(planId, patientId, doctorId, status);
        List<SessionSummaryResponse> content = sessionReadMapper.findAll(
                planId, patientId, doctorId, status,
                (int) pageable.getOffset(), pageable.getPageSize()
        );
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionSummaryResponse> getByPlanId(Long planId) {
        return sessionReadMapper.findByPlanId(planId);
    }

    private ExerciseLog buildExerciseLog(ExerciseLogRequest req, TherapySession session) {
        TherapyExercise exercise = exerciseRepository.findById(req.exerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + req.exerciseId()));
        ExerciseLog log = new ExerciseLog();
        log.setSession(session);
        log.setExercise(exercise);
        log.setCompletionStatus(req.completionStatus());
        log.setActualDurationMinutes(req.actualDurationMinutes());
        log.setDifficultyRating(req.difficultyRating());
        log.setPatientNotes(req.patientNotes());
        log.setDoctorNotes(req.doctorNotes());
        return log;
    }
}
