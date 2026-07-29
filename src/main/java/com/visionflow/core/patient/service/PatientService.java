package com.visionflow.core.patient.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.patient.dto.request.AssignDoctorRequest;
import com.visionflow.core.patient.dto.request.CreatePatientRequest;
import com.visionflow.core.patient.dto.request.UpdatePatientRequest;
import com.visionflow.core.patient.dto.response.PatientResponse;
import com.visionflow.core.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    PatientResponse create(CreatePatientRequest request);

    PatientResponse update(Long id, UpdatePatientRequest request);

    PatientResponse assignDoctor(Long id, AssignDoctorRequest request);

    PatientResponse getById(Long id);

    PatientResponse getByUserId(Long userId);

    Patient getEntityById(Long id);

    Page<PatientResponse> getAll(Long doctorId, String gender, Boolean active, Pageable pageable);

    void deactivate(Long id);
}
