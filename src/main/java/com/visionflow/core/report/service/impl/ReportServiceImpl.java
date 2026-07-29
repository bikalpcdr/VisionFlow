package com.visionflow.core.report.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.report.dto.response.ClinicOverviewReport;
import com.visionflow.core.report.dto.response.DoctorWorkloadReport;
import com.visionflow.core.report.dto.response.PatientProgressReport;
import com.visionflow.core.report.repo.ReportReadMapper;
import com.visionflow.core.report.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportReadMapper reportReadMapper;

    @Override
    @Transactional(readOnly = true)
    public PatientProgressReport getPatientProgress(Long patientId) {
        return reportReadMapper.getPatientProgress(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorWorkloadReport getDoctorWorkload(Long doctorId) {
        return reportReadMapper.getDoctorWorkload(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));
    }

    @Override
    @Transactional(readOnly = true)
    public ClinicOverviewReport getClinicOverview(LocalDate from, LocalDate to) {
        return reportReadMapper.getClinicOverview(from, to);
    }
}
