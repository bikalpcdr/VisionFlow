package com.visionflow.core.therapyplan.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.dto.request.CreateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.request.ExerciseRequest;
import com.visionflow.core.therapyplan.dto.request.UpdateTherapyPlanRequest;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanResponse;
import com.visionflow.core.therapyplan.dto.response.TherapyPlanSummaryResponse;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TherapyPlanService {

    TherapyPlanResponse create(CreateTherapyPlanRequest request);

    TherapyPlanResponse update(Long id, UpdateTherapyPlanRequest request);

    TherapyPlanResponse addExercise(Long planId, ExerciseRequest request);

    TherapyPlanResponse removeExercise(Long planId, Long exerciseId);

    TherapyPlanResponse getById(Long id);

    TherapyPlan getEntityById(Long id);

    Page<TherapyPlanSummaryResponse> getAll(Long patientId, Long doctorId, String status, Pageable pageable);

    List<TherapyPlanSummaryResponse> getByPatientId(Long patientId);

    void cancel(Long id);
}
