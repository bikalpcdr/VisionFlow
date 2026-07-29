package com.visionflow.core.therapyplan.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.dto.response.TherapyPlanResponse;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TherapyPlanReadMapper {

    Optional<TherapyPlanResponse> findById(@Param("id") Long id);

    List<TherapyPlanSummaryResponse> findAll(@Param("patientId") Long patientId,
                                             @Param("doctorId") Long doctorId,
                                             @Param("status") String status,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    long countAll(@Param("patientId") Long patientId,
                  @Param("doctorId") Long doctorId,
                  @Param("status") String status);

    List<TherapyPlanSummaryResponse> findByPatientId(@Param("patientId") Long patientId);
}
