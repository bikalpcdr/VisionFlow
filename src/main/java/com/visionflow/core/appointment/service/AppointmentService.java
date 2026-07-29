package com.visionflow.core.appointment.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(CreateAppointmentRequest request);

    AppointmentResponse update(Long id, UpdateAppointmentRequest request);

    AppointmentResponse getById(Long id);

    Appointment getEntityById(Long id);

    Page<AppointmentSummaryResponse> getAll(Long patientId, Long doctorId, String status, String appointmentType, Pageable pageable);

    List<AppointmentSummaryResponse> getByPatientId(Long patientId);

    List<AppointmentSummaryResponse> getByDoctorId(Long doctorId);

    AppointmentResponse confirm(Long id);

    AppointmentResponse start(Long id);

    AppointmentResponse complete(Long id);

    void cancel(Long id, CancelAppointmentRequest request);

    void noShow(Long id);
}
