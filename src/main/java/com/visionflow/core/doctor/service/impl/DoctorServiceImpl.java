package com.visionflow.core.doctor.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.entity.User;
import com.visionflow.core.auth.enums.Role;
import com.visionflow.core.auth.service.UserService;
import com.visionflow.core.doctor.dto.request.CreateDoctorRequest;
import com.visionflow.core.doctor.dto.request.UpdateDoctorRequest;
import com.visionflow.core.doctor.dto.response.DoctorResponse;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.doctor.mapper.DoctorMapper;
import com.visionflow.core.doctor.repo.DoctorReadMapper;
import com.visionflow.core.doctor.repo.DoctorRepository;
import com.visionflow.core.doctor.service.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorReadMapper doctorReadMapper;
    private final DoctorMapper doctorMapper;
    private final UserService userService;

    @Override
    @Transactional
    public DoctorResponse create(CreateDoctorRequest request) {
        User user = userService.getEntityById(request.userId());

        if (user.getRole() != Role.DOCTOR) {
            throw new GenericUncheckedException(
                    "User must have DOCTOR role to create a doctor profile", "INVALID_ROLE", HttpStatus.BAD_REQUEST.value());
        }

        if (doctorRepository.existsByUserIdAndDeletedFalse(request.userId())) {
            throw new GenericUncheckedException(
                    "Doctor profile already exists for this user", "DOCTOR_EXISTS", HttpStatus.CONFLICT.value());
        }

        if (doctorRepository.existsByLicenseNumberAndDeletedFalse(request.licenseNumber())) {
            throw new GenericUncheckedException(
                    "License number already registered", "LICENSE_TAKEN", HttpStatus.CONFLICT.value());
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(request.specialization());
        doctor.setLicenseNumber(request.licenseNumber());
        doctor.setQualification(request.qualification());
        doctor.setYearsOfExperience(request.yearsOfExperience());
        doctor.setBio(request.bio());
        doctor.setClinicName(request.clinicName());
        doctor.setClinicAddress(request.clinicAddress());

        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor profile created: id={}, userId={}", saved.getId(), request.userId());

        return doctorReadMapper.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found after creation"));
    }

    @Override
    @Transactional
    public DoctorResponse update(Long id, UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        doctorMapper.updateEntity(request, doctor);
        doctorRepository.save(doctor);
        log.info("Doctor profile updated: id={}", id);

        return doctorReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found after update"));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getById(Long id) {
        return doctorReadMapper.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getByUserId(Long userId) {
        return doctorReadMapper.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found for user id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAll(String specialization, Boolean active, Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        List<DoctorResponse> content = doctorReadMapper.findAll(specialization, active, offset, limit);
        long total = doctorReadMapper.countAll(specialization, active);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
        doctor.setActive(false);
        doctorRepository.save(doctor);
        log.info("Doctor deactivated: id={}", id);
    }
}
