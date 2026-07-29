package com.visionflow.core.report.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.report.dto.response.ClinicOverviewReport;
import com.visionflow.core.report.dto.response.DoctorWorkloadReport;
import com.visionflow.core.report.dto.response.PatientProgressReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Optional;

@Mapper
public interface ReportReadMapper {

    Optional<PatientProgressReport> getPatientProgress(@Param("patientId") Long patientId);

    Optional<DoctorWorkloadReport> getDoctorWorkload(@Param("doctorId") Long doctorId);

    ClinicOverviewReport getClinicOverview(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
