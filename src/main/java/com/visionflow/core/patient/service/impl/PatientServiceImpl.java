package com.visionflow.core.patient.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.entity.User;
import com.visionflow.core.auth.enums.Role;
import com.visionflow.core.auth.service.UserService;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.doctor.repo.DoctorRepository;
import com.visionflow.core.patient.dto.request.AssignDoctorRequest;
import com.visionflow.core.patient.dto.request.CreatePatientRequest;
import com.visionflow.core.patient.dto.request.UpdatePatientRequest;
import com.visionflow.core.patient.dto.response.PatientResponse;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.patient.mapper.PatientMapper;
import com.visionflow.core.patient.repo.PatientReadMapper;
import com.visionflow.core.patient.repo.PatientRepository;
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
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientReadMapper patientReadMapper;
    private final PatientMapper patientMapper;
    private final UserService userService;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        User user = userService.getEntityById(request.userId());

        if (user.getRole() != Role.PATIENT) {
            throw new GenericUncheckedException(
                    "User must have PATIENT role to create a patient profile",
                    "INVALID_ROLE", HttpStatus.BAD_REQUEST.value());
        }

        if (patientRepository.existsByUserIdAndDeletedFalse(request.userId())) {
            throw new GenericUncheckedException(
                    "Patient profile already exists for this user",
                    "PATIENT_EXISTS", HttpStatus.CONFLICT.value());
        }

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setBloodGroup(request.bloodGroup());
        patient.setAddress(request.address());
        patient.setEmergencyContactName(request.emergencyContactName());
        patient.setEmergencyContactPhone(request.emergencyContactPhone());
        patient.setAllergies(request.allergies());
        patient.setMedicalNotes(request.medicalNotes());

        if (request.assignedDoctorId() != null) {
            Doctor doctor = doctorRepository.findByIdAndDeletedFalse(request.assignedDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Doctor not found with id: " + request.assignedDoctorId()));
            patient.setAssignedDoctor(doctor);
        }

        Patient saved = patientRepository.save(patient);
        log.info("Patient profile created: id={}, userId={}", saved.getId(), request.userId());

        return patientReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found after creation"));
    }

    @Override
    @Transactional
    public PatientResponse update(Long id, UpdatePatientRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        patientMapper.updateEntity(request, patient);
        patientRepository.save(patient);
        log.info("Patient profile updated: id={}", id);

        return patientReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found after update"));
    }

    @Override
    @Transactional
    public PatientResponse assignDoctor(Long id, AssignDoctorRequest request) {
        Patient patient = patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(request.doctorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Doctor not found with id: " + request.doctorId()));

        patient.setAssignedDoctor(doctor);
        patientRepository.save(patient);
        log.info("Doctor id={} assigned to patient id={}", request.doctorId(), id);

        return patientReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found after doctor assignment"));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getById(Long id) {
        return patientReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getByUserId(Long userId) {
        return patientReadMapper.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Patient profile not found for user id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Patient getEntityById(Long id) {
        return patientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> getAll(Long doctorId, String gender, Boolean active, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        List<PatientResponse> content = patientReadMapper.findAll(doctorId, gender, active, offset, limit);
        long total = patientReadMapper.countAll(doctorId, gender, active);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Patient patient = getEntityById(id);
        patient.setActive(false);
        patientRepository.save(patient);
        log.info("Patient deactivated: id={}", id);
    }
}
