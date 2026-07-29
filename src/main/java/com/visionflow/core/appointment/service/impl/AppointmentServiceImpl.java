package com.visionflow.core.appointment.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.dto.request.CancelAppointmentRequest;
import com.visionflow.core.appointment.dto.request.CreateAppointmentRequest;
import com.visionflow.core.appointment.dto.request.UpdateAppointmentRequest;
import com.visionflow.core.appointment.dto.response.AppointmentResponse;
import com.visionflow.core.appointment.dto.response.AppointmentSummaryResponse;
import com.visionflow.core.appointment.entity.Appointment;
import com.visionflow.core.appointment.enums.AppointmentStatus;
import com.visionflow.core.appointment.mapper.AppointmentMapper;
import com.visionflow.core.appointment.repo.AppointmentReadMapper;
import com.visionflow.core.appointment.repo.AppointmentRepository;
import com.visionflow.core.appointment.service.AppointmentService;
import com.visionflow.core.assessment.service.AssessmentService;
import com.visionflow.core.doctor.service.DoctorService;
import com.visionflow.core.notification.enums.NotificationType;
import com.visionflow.core.notification.enums.ReferenceType;
import com.visionflow.core.notification.service.NotificationService;
import com.visionflow.core.patient.service.PatientService;
import com.visionflow.core.therapyplan.service.TherapyPlanService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentReadMapper appointmentReadMapper;
    private final AppointmentMapper appointmentMapper;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AssessmentService assessmentService;
    private final TherapyPlanService therapyPlanService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request) {
        LocalDateTime end = request.appointmentDate().plusMinutes(request.durationMinutes());

        if (appointmentRepository.existsDoctorConflict(request.doctorId(), request.appointmentDate(), end)) {
            throw new IllegalStateException("Doctor has a conflicting appointment in this time slot.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patientService.getEntityById(request.patientId()));
        appointment.setDoctor(doctorService.getEntityById(request.doctorId()));
        appointment.setAppointmentType(request.appointmentType());
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setDurationMinutes(request.durationMinutes());
        appointment.setPatientNotes(request.patientNotes());
        appointment.setStatus(AppointmentStatus.REQUESTED);

        if (request.assessmentId() != null) {
            appointment.setAssessment(assessmentService.getEntityById(request.assessmentId()));
        }
        if (request.therapyPlanId() != null) {
            appointment.setTherapyPlan(therapyPlanService.getEntityById(request.therapyPlanId()));
        }

        Appointment saved = appointmentRepository.save(appointment);
        notificationService.send(
                saved.getDoctor().getUser().getId(),
                NotificationType.APPOINTMENT_REQUESTED,
                "New Appointment Request",
                "Patient " + saved.getPatient().getUser().getFirstName() + " " + saved.getPatient().getUser().getLastName() + " has requested an appointment on " + saved.getAppointmentDate() + ".",
                ReferenceType.APPOINTMENT, saved.getId());
        return appointmentReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found after save"));
    }

    @Override
    @Transactional
    public AppointmentResponse update(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = getEntityById(id);
        if (isTerminal(appointment.getStatus())) {
            throw new IllegalStateException("Cannot update a " + appointment.getStatus() + " appointment.");
        }
        if (request.appointmentDate() != null) {
            LocalDateTime newEnd = request.appointmentDate()
                    .plusMinutes(request.durationMinutes() != null ? request.durationMinutes() : appointment.getDurationMinutes());
            if (appointmentRepository.existsDoctorConflict(appointment.getDoctor().getId(), request.appointmentDate(), newEnd)) {
                throw new IllegalStateException("Doctor has a conflicting appointment in this time slot.");
            }
        }
        appointmentMapper.updateAppointment(request, appointment);
        appointmentRepository.save(appointment);
        return appointmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional
    public AppointmentResponse confirm(Long id) {
        Appointment appointment = getEntityById(id);
        appointment.setStatus(switch (appointment.getStatus()) {
            case REQUESTED -> AppointmentStatus.CONFIRMED;
            case CONFIRMED -> throw new IllegalStateException("Appointment is already CONFIRMED.");
            case IN_PROGRESS -> throw new IllegalStateException("Appointment is already IN_PROGRESS.");
            case COMPLETED -> throw new IllegalStateException("Appointment is already COMPLETED.");
            case CANCELLED -> throw new IllegalStateException("Cannot confirm a CANCELLED appointment.");
            case NO_SHOW -> throw new IllegalStateException("Cannot confirm a NO_SHOW appointment.");
        });
        appointmentRepository.save(appointment);
        notificationService.send(
                appointment.getPatient().getUser().getId(),
                NotificationType.APPOINTMENT_CONFIRMED,
                "Appointment Confirmed",
                "Your appointment on " + appointment.getAppointmentDate() + " has been confirmed.",
                ReferenceType.APPOINTMENT, id);
        return appointmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional
    public AppointmentResponse start(Long id) {
        Appointment appointment = getEntityById(id);
        appointment.setStatus(switch (appointment.getStatus()) {
            case CONFIRMED -> AppointmentStatus.IN_PROGRESS;
            case REQUESTED -> throw new IllegalStateException("Appointment must be CONFIRMED before starting.");
            case IN_PROGRESS -> throw new IllegalStateException("Appointment is already IN_PROGRESS.");
            case COMPLETED -> throw new IllegalStateException("Appointment is already COMPLETED.");
            case CANCELLED -> throw new IllegalStateException("Cannot start a CANCELLED appointment.");
            case NO_SHOW -> throw new IllegalStateException("Cannot start a NO_SHOW appointment.");
        });
        appointmentRepository.save(appointment);
        return appointmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional
    public AppointmentResponse complete(Long id) {
        Appointment appointment = getEntityById(id);
        appointment.setStatus(switch (appointment.getStatus()) {
            case IN_PROGRESS -> AppointmentStatus.COMPLETED;
            case REQUESTED, CONFIRMED ->
                    throw new IllegalStateException("Appointment must be IN_PROGRESS before completing.");
            case COMPLETED -> throw new IllegalStateException("Appointment is already COMPLETED.");
            case CANCELLED -> throw new IllegalStateException("Cannot complete a CANCELLED appointment.");
            case NO_SHOW -> throw new IllegalStateException("Cannot complete a NO_SHOW appointment.");
        });
        appointmentRepository.save(appointment);
        return appointmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional
    public void cancel(Long id, CancelAppointmentRequest request) {
        Appointment appointment = getEntityById(id);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a COMPLETED appointment.");
        }
        if (isTerminal(appointment.getStatus())) {
            throw new IllegalStateException("Appointment is already " + appointment.getStatus() + ".");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(request != null ? request.cancellationReason() : null);
        appointmentRepository.save(appointment);
        notificationService.send(
                appointment.getPatient().getUser().getId(),
                NotificationType.APPOINTMENT_CANCELLED,
                "Appointment Cancelled",
                "Your appointment on " + appointment.getAppointmentDate() + " has been cancelled.",
                ReferenceType.APPOINTMENT, id);
    }

    @Override
    @Transactional
    public void noShow(Long id) {
        Appointment appointment = getEntityById(id);
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED appointments can be marked as NO_SHOW.");
        }
        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        return appointmentReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getEntityById(Long id) {
        return appointmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentSummaryResponse> getAll(Long patientId, Long doctorId, String status, String appointmentType, Pageable pageable) {
        long total = appointmentReadMapper.countAll(patientId, doctorId, status, appointmentType);
        List<AppointmentSummaryResponse> content = appointmentReadMapper.findAll(
                patientId, doctorId, status, appointmentType,
                (int) pageable.getOffset(), pageable.getPageSize()
        );
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryResponse> getByPatientId(Long patientId) {
        return appointmentReadMapper.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSummaryResponse> getByDoctorId(Long doctorId) {
        return appointmentReadMapper.findByDoctorId(doctorId);
    }

    private boolean isTerminal(AppointmentStatus status) {
        return status == AppointmentStatus.CANCELLED || status == AppointmentStatus.NO_SHOW;
    }
}
