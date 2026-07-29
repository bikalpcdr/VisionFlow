package com.visionflow.core.report.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.report.dto.response.ClinicOverviewReport;
import com.visionflow.core.report.dto.response.DoctorWorkloadReport;
import com.visionflow.core.report.dto.response.PatientProgressReport;

import java.time.LocalDate;

public interface ReportService {

    PatientProgressReport getPatientProgress(Long patientId);

    DoctorWorkloadReport getDoctorWorkload(Long doctorId);

    ClinicOverviewReport getClinicOverview(LocalDate from, LocalDate to);
}
