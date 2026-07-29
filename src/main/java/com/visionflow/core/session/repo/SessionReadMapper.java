package com.visionflow.core.session.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.dto.response.SessionResponse;
import com.visionflow.core.session.dto.response.SessionSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SessionReadMapper {

    Optional<SessionResponse> findById(@Param("id") Long id);

    List<SessionSummaryResponse> findAll(@Param("planId") Long planId,
                                         @Param("patientId") Long patientId,
                                         @Param("doctorId") Long doctorId,
                                         @Param("status") String status,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    long countAll(@Param("planId") Long planId,
                  @Param("patientId") Long patientId,
                  @Param("doctorId") Long doctorId,
                  @Param("status") String status);

    List<SessionSummaryResponse> findByPlanId(@Param("planId") Long planId);
}
