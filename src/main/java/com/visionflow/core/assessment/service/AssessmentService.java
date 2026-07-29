package com.visionflow.core.assessment.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.dto.request.CreateAssessmentRequest;
import com.visionflow.core.assessment.dto.request.UpdateAssessmentRequest;
import com.visionflow.core.assessment.dto.response.AssessmentResponse;
import com.visionflow.core.assessment.dto.response.AssessmentSummaryResponse;
import com.visionflow.core.assessment.entity.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssessmentService {

    AssessmentResponse create(CreateAssessmentRequest request);

    AssessmentResponse update(Long id, UpdateAssessmentRequest request);

    AssessmentResponse getById(Long id);

    Assessment getEntityById(Long id);

    Page<AssessmentSummaryResponse> getAll(Long patientId, Long doctorId,
                                           String assessmentType, String status,
                                           Pageable pageable);

    List<AssessmentSummaryResponse> getByPatientId(Long patientId);

    void cancel(Long id);
}
