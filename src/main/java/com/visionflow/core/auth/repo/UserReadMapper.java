package com.visionflow.core.auth.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.dto.response.UserResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * Read-side mapper.
 * All queries are defined in UserReadMapper.xml.
 * Returns DTOs/projections directly — never JPA entities.
 * Named UserReadMapper (not UserMapper) to avoid collision with the
 * MapStruct UserMapper in the core.auth.mapper package.
 */
@Mapper
public interface UserReadMapper {

    Optional<UserResponse> findById(@Param("id") Long id);

    List<UserResponse> findAll(@Param("role") String role,
                               @Param("active") Boolean active,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countAll(@Param("role") String role, @Param("active") Boolean active);
}
