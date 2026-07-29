package com.visionflow.core.assessment.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.dto.response.AssessmentResponse;
import com.visionflow.core.assessment.dto.response.AssessmentSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AssessmentReadMapper {

    Optional<AssessmentResponse> findById(@Param("id") Long id);

    List<AssessmentSummaryResponse> findAll(@Param("patientId") Long patientId,
                                            @Param("doctorId") Long doctorId,
                                            @Param("assessmentType") String assessmentType,
                                            @Param("status") String status,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    long countAll(@Param("patientId") Long patientId,
                  @Param("doctorId") Long doctorId,
                  @Param("assessmentType") String assessmentType,
                  @Param("status") String status);

    List<AssessmentSummaryResponse> findByPatientId(@Param("patientId") Long patientId);
}
