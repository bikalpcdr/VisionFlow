package com.visionflow.core.patient.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.patient.dto.response.PatientResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PatientReadMapper {

    Optional<PatientResponse> findById(@Param("id") Long id);

    Optional<PatientResponse> findByUserId(@Param("userId") Long userId);

    List<PatientResponse> findAll(@Param("doctorId") Long doctorId,
                                  @Param("gender") String gender,
                                  @Param("active") Boolean active,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countAll(@Param("doctorId") Long doctorId,
                  @Param("gender") String gender,
                  @Param("active") Boolean active);
}
