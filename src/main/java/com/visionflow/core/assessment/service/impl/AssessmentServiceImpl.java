package com.visionflow.core.assessment.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.dto.request.CreateAssessmentRequest;
import com.visionflow.core.assessment.dto.request.UpdateAssessmentRequest;
import com.visionflow.core.assessment.dto.response.AssessmentResponse;
import com.visionflow.core.assessment.dto.response.AssessmentSummaryResponse;
import com.visionflow.core.assessment.entity.Assessment;
import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.mapper.AssessmentMapper;
import com.visionflow.core.assessment.repo.AssessmentReadMapper;
import com.visionflow.core.assessment.repo.AssessmentRepository;
import com.visionflow.core.assessment.service.AssessmentService;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.doctor.repo.DoctorRepository;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.patient.service.PatientService;
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
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentReadMapper assessmentReadMapper;
    private final AssessmentMapper assessmentMapper;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public AssessmentResponse create(CreateAssessmentRequest request) {
        Patient patient = patientService.getEntityById(request.patientId());

        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(request.doctorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Doctor not found with id: " + request.doctorId()));

        Assessment assessment = new Assessment();
        assessment.setPatient(patient);
        assessment.setDoctor(doctor);
        assessment.setAssessmentType(request.assessmentType());
        assessment.setStatus(AssessmentStatus.DRAFT);
        assessment.setAssessmentDate(request.assessmentDate());
        assessment.setVisualAcuityLeft(request.visualAcuityLeft());
        assessment.setVisualAcuityRight(request.visualAcuityRight());
        assessment.setVisualAcuityBinocular(request.visualAcuityBinocular());
        assessment.setSphereLeft(request.sphereLeft());
        assessment.setSphereRight(request.sphereRight());
        assessment.setCylinderLeft(request.cylinderLeft());
        assessment.setCylinderRight(request.cylinderRight());
        assessment.setAxisLeft(request.axisLeft());
        assessment.setAxisRight(request.axisRight());
        assessment.setCoverTestResult(request.coverTestResult());
        assessment.setNpcResult(request.npcResult());
        assessment.setStereopsisResult(request.stereopsisResult());
        assessment.setSeverity(request.severity());
        assessment.setAffectedEye(request.affectedEye());
        assessment.setIopLeft(request.iopLeft());
        assessment.setIopRight(request.iopRight());
        assessment.setChiefComplaint(request.chiefComplaint());
        assessment.setClinicalFindings(request.clinicalFindings());
        assessment.setRecommendations(request.recommendations());
        assessment.setFollowUpDate(request.followUpDate());

        Assessment saved = assessmentRepository.save(assessment);
        log.info("Assessment created: id={}, patientId={}, type={}", saved.getId(),
                request.patientId(), request.assessmentType());

        return assessmentReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found after creation"));
    }

    @Override
    @Transactional
    public AssessmentResponse update(Long id, UpdateAssessmentRequest request) {
        Assessment assessment = assessmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found with id: " + id));

        validateStatusTransition(assessment.getStatus(), request.status());

        assessmentMapper.updateEntity(request, assessment);
        assessmentRepository.save(assessment);
        log.info("Assessment updated: id={}", id);

        return assessmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found after update"));
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getById(Long id) {
        return assessmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Assessment getEntityById(Long id) {
        return assessmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssessmentSummaryResponse> getAll(Long patientId,
                                                  Long doctorId,
                                                  String assessmentType,
                                                  String status,
                                                  Pageable pageable
    ) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        List<AssessmentSummaryResponse> content =
                assessmentReadMapper.findAll(patientId, doctorId, assessmentType, status, offset, limit);
        long total = assessmentReadMapper.countAll(patientId, doctorId, assessmentType, status);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentSummaryResponse> getByPatientId(Long patientId) {
        patientService.getEntityById(patientId);
        return assessmentReadMapper.findByPatientId(patientId);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Assessment assessment = assessmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found with id: " + id));

        if (assessment.getStatus() == AssessmentStatus.REVIEWED) {
            throw new GenericUncheckedException(
                    "Cannot cancel a reviewed assessment", "INVALID_STATUS_TRANSITION", HttpStatus.CONFLICT.value());
        }

        assessment.setStatus(AssessmentStatus.CANCELLED);
        assessmentRepository.save(assessment);
        log.info("Assessment cancelled: id={}", id);
    }

    private void validateStatusTransition(AssessmentStatus current, AssessmentStatus next) {
        if (next == null) return;

        boolean invalid = switch (current) {
            case DRAFT -> next != AssessmentStatus.COMPLETED && next != AssessmentStatus.CANCELLED;
            case COMPLETED -> next != AssessmentStatus.REVIEWED && next != AssessmentStatus.CANCELLED;
            case REVIEWED, CANCELLED -> true;
        };

        if (invalid) {
            throw new GenericUncheckedException(
                    String.format("Invalid status transition: %s → %s", current, next),
                    "INVALID_STATUS_TRANSITION", HttpStatus.CONFLICT.value());
        }
    }
}
