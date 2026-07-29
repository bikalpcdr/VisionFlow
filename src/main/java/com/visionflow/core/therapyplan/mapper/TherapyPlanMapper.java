package com.visionflow.core.therapyplan.mapper;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.dto.request.ExerciseRequest;
import com.visionflow.core.therapyplan.dto.request.UpdateTherapyPlanRequest;
import com.visionflow.core.therapyplan.entity.TherapyExercise;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TherapyPlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "assessment", ignore = true)
    @Mapping(target = "exercises", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updatePlan(UpdateTherapyPlanRequest request, @MappingTarget TherapyPlan plan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "therapyPlan", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    TherapyExercise toExerciseEntity(ExerciseRequest request);
}
