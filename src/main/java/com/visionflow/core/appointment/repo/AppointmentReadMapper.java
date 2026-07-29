package com.visionflow.core.appointment.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.dto.response.AppointmentResponse;
import com.visionflow.core.appointment.dto.response.AppointmentSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentReadMapper {

    Optional<AppointmentResponse> findById(@Param("id") Long id);

    List<AppointmentSummaryResponse> findAll(@Param("patientId") Long patientId,
                                             @Param("doctorId") Long doctorId,
                                             @Param("status") String status,
                                             @Param("appointmentType") String appointmentType,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    long countAll(@Param("patientId") Long patientId,
                  @Param("doctorId") Long doctorId,
                  @Param("status") String status,
                  @Param("appointmentType") String appointmentType);

    List<AppointmentSummaryResponse> findByPatientId(@Param("patientId") Long patientId);

    List<AppointmentSummaryResponse> findByDoctorId(@Param("doctorId") Long doctorId);
}
