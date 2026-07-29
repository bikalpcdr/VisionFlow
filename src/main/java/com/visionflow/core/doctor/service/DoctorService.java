package com.visionflow.core.doctor.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.dto.request.CreateDoctorRequest;
import com.visionflow.core.doctor.dto.request.UpdateDoctorRequest;
import com.visionflow.core.doctor.dto.response.DoctorResponse;
import com.visionflow.core.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {

    DoctorResponse create(CreateDoctorRequest request);

    DoctorResponse update(Long id, UpdateDoctorRequest request);

    DoctorResponse getById(Long id);

    Doctor getEntityById(Long id);

    DoctorResponse getByUserId(Long userId);

    Page<DoctorResponse> getAll(String specialization, Boolean active, Pageable pageable);

    void deactivate(Long id);
}
