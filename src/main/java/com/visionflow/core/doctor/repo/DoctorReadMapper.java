package com.visionflow.core.doctor.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.dto.response.DoctorResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DoctorReadMapper {

    Optional<DoctorResponse> findById(@Param("id") Long id);

    Optional<DoctorResponse> findByUserId(@Param("userId") Long userId);

    List<DoctorResponse> findAll(@Param("specialization") String specialization,
                                 @Param("active") Boolean active,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countAll(@Param("specialization") String specialization,
                  @Param("active") Boolean active);
}
