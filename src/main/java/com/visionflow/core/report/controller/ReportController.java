package com.visionflow.core.report.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.report.dto.response.ClinicOverviewReport;
import com.visionflow.core.report.dto.response.DoctorWorkloadReport;
import com.visionflow.core.report.dto.response.PatientProgressReport;
import com.visionflow.core.report.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/patients/{patientId}/progress")
    @SuccessMessage(entity = MessageConstant.PROGRESS, action = MessageConstant.FETCHED)
    public PatientProgressReport getPatientProgress(@PathVariable Long patientId) {
        return reportService.getPatientProgress(patientId);
    }

    @GetMapping("/doctors/{doctorId}/workload")
    @SuccessMessage(entity = MessageConstant.DOCTOR, action = MessageConstant.FETCHED)
    public DoctorWorkloadReport getDoctorWorkload(@PathVariable Long doctorId) {
        return reportService.getDoctorWorkload(doctorId);
    }

    @GetMapping("/clinic/overview")
    @SuccessMessage(entity = MessageConstant.CLINIC_OVERVIEW, action = MessageConstant.FETCHED)
    public ClinicOverviewReport getClinicOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return reportService.getClinicOverview(from, to);
    }
}
