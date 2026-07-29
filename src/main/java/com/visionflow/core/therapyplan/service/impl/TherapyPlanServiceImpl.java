package com.visionflow.core.therapyplan.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.entity.Assessment;
import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.service.AssessmentService;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.doctor.repo.DoctorRepository;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.patient.service.PatientService;
import com.visionflow.core.therapyplan.dto.request.CreateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.request.ExerciseRequest;
import com.visionflow.core.therapyplan.dto.request.UpdateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanResponse;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanSummaryResponse;
import com.visionflow.core.therapyplan.entity.TherapyExercise;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import com.visionflow.core.therapyplan.mapper.TherapyPlanMapper;
import com.visionflow.core.therapyplan.repo.TherapyExerciseRepository;
import com.visionflow.core.therapyplan.repo.TherapyPlanReadMapper;
import com.visionflow.core.therapyplan.repo.TherapyPlanRepository;
import com.visionflow.core.therapyplan.service.TherapyPlanService;
import com.visionflow.exception.GenericUncheckedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapyPlanServiceImpl implements TherapyPlanService {

    private final TherapyPlanRepository therapyPlanRepository;
    private final TherapyExerciseRepository therapyExerciseRepository;
    private final TherapyPlanReadMapper therapyPlanReadMapper;
    private final TherapyPlanMapper therapyPlanMapper;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final AssessmentService assessmentService;

    @Override
    @Transactional
    public TherapyPlanResponse create(CreateTherapyPlanRequest request) {
        Patient patient = patientService.getEntityById(request.patientId());

        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(request.doctorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Doctor not found with id: " + request.doctorId()));

        if (therapyPlanRepository.existsByPatientIdAndStatusAndDeletedFalse(
                request.patientId(), TherapyPlanStatus.ACTIVE)) {
            throw new GenericUncheckedException(
                    "Patient already has an active therapy plan",
                    "ACTIVE_PLAN_EXISTS", HttpStatus.CONFLICT.value());
        }

        TherapyPlan plan = new TherapyPlan();
        plan.setPatient(patient);
        plan.setDoctor(doctor);
        plan.setTitle(request.title());
        plan.setDescription(request.description());
        plan.setStatus(TherapyPlanStatus.DRAFT);
        plan.setStartDate(request.startDate());
        plan.setEndDate(request.endDate());
        plan.setGoals(request.goals());
        plan.setNotes(request.notes());

        if (request.assessmentId() != null) {
            Assessment assessment = assessmentService.getEntityById(request.assessmentId());
            if (assessment.getStatus() != AssessmentStatus.COMPLETED
                    && assessment.getStatus() != AssessmentStatus.REVIEWED) {
                throw new GenericUncheckedException(
                        "Assessment must be COMPLETED or REVIEWED before linking to a therapy plan",
                        "INVALID_ASSESSMENT_STATUS", HttpStatus.BAD_REQUEST.value());
            }
            plan.setAssessment(assessment);
        }

        request.exercises().forEach(exerciseRequest -> {
            TherapyExercise exercise = therapyPlanMapper.toExerciseEntity(exerciseRequest);
            exercise.setTherapyPlan(plan);
            plan.getExercises().add(exercise);
        });

        TherapyPlan saved = therapyPlanRepository.save(plan);
        log.info("Therapy plan created: id={}, patientId={}", saved.getId(), request.patientId());

        return therapyPlanReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found after creation"));
    }

    @Override
    @Transactional
    public TherapyPlanResponse update(Long id, UpdateTherapyPlanRequest request) {
        TherapyPlan plan = therapyPlanRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + id));

        if (plan.getStatus() == TherapyPlanStatus.CANCELLED) {
            throw new GenericUncheckedException(
                    "Cannot update a cancelled therapy plan",
                    "PLAN_CANCELLED", HttpStatus.CONFLICT.value());
        }

        validateStatusTransition(plan.getStatus(), request.status());
        therapyPlanMapper.updatePlan(request, plan);
        therapyPlanRepository.save(plan);
        log.info("Therapy plan updated: id={}", id);

        return therapyPlanReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found after update"));
    }

    @Override
    @Transactional
    public TherapyPlanResponse addExercise(Long planId, ExerciseRequest request) {
        TherapyPlan plan = therapyPlanRepository.findByIdWithExercises(planId)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + planId));

        if (plan.getStatus() == TherapyPlanStatus.COMPLETED
                || plan.getStatus() == TherapyPlanStatus.CANCELLED) {
            throw new GenericUncheckedException(
                    "Cannot add exercises to a " + plan.getStatus().name().toLowerCase() + " plan",
                    "INVALID_PLAN_STATUS", HttpStatus.CONFLICT.value());
        }

        TherapyExercise exercise = therapyPlanMapper.toExerciseEntity(request);
        exercise.setTherapyPlan(plan);
        plan.getExercises().add(exercise);
        therapyPlanRepository.save(plan);
        log.info("Exercise added to therapy plan: planId={}", planId);

        return therapyPlanReadMapper.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found after adding exercise"));
    }

    @Override
    @Transactional
    public TherapyPlanResponse removeExercise(Long planId, Long exerciseId) {
        TherapyPlan plan = therapyPlanRepository.findByIdWithExercises(planId)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + planId));

        if (plan.getStatus() == TherapyPlanStatus.COMPLETED
                || plan.getStatus() == TherapyPlanStatus.CANCELLED) {
            throw new GenericUncheckedException(
                    "Cannot remove exercises from a " + plan.getStatus().name().toLowerCase() + " plan",
                    "INVALID_PLAN_STATUS", HttpStatus.CONFLICT.value());
        }

        TherapyExercise exercise = therapyExerciseRepository
                .findByIdAndTherapyPlanIdAndDeletedFalse(exerciseId, planId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Exercise not found with id: " + exerciseId + " in plan: " + planId));

        plan.getExercises().remove(exercise);
        therapyPlanRepository.save(plan);
        log.info("Exercise id={} removed from therapy plan id={}", exerciseId, planId);

        return therapyPlanReadMapper.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found after removing exercise"));
    }

    @Override
    @Transactional(readOnly = true)
    public TherapyPlanResponse getById(Long id) {
        return therapyPlanReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TherapyPlan getEntityById(Long id) {
        return therapyPlanRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TherapyPlanSummaryResponse> getAll(Long patientId, Long doctorId,
                                                   String status, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        List<TherapyPlanSummaryResponse> content =
                therapyPlanReadMapper.findAll(patientId, doctorId, status, offset, limit);
        long total = therapyPlanReadMapper.countAll(patientId, doctorId, status);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TherapyPlanSummaryResponse> getByPatientId(Long patientId) {
        patientService.getEntityById(patientId);
        return therapyPlanReadMapper.findByPatientId(patientId);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        TherapyPlan plan = therapyPlanRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Therapy plan not found with id: " + id));

        if (plan.getStatus() == TherapyPlanStatus.COMPLETED
                || plan.getStatus() == TherapyPlanStatus.CANCELLED) {
            throw new GenericUncheckedException(
                    "Cannot cancel a " + plan.getStatus().name().toLowerCase() + " plan",
                    "INVALID_STATUS_TRANSITION", HttpStatus.CONFLICT.value());
        }

        plan.setStatus(TherapyPlanStatus.CANCELLED);
        therapyPlanRepository.save(plan);
        log.info("Therapy plan cancelled: id={}", id);
    }

    private void validateStatusTransition(TherapyPlanStatus current, TherapyPlanStatus next) {
        if (next == null) return;

        boolean invalid = switch (current) {
            case DRAFT -> next != TherapyPlanStatus.ACTIVE && next != TherapyPlanStatus.CANCELLED;
            case ACTIVE -> next != TherapyPlanStatus.COMPLETED && next != TherapyPlanStatus.CANCELLED;
            case COMPLETED, CANCELLED -> true;
        };

        if (invalid) {
            throw new GenericUncheckedException(
                    String.format("Invalid status transition: %s → %s", current, next),
                    "INVALID_STATUS_TRANSITION", HttpStatus.CONFLICT.value());
        }
    }
}
