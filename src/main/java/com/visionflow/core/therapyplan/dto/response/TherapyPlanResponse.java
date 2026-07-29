package com.visionflow.core.therapyplan.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TherapyPlanResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long assessmentId;
    private String title;
    private String description;
    private TherapyPlanStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goals;
    private String notes;
    private List<ExerciseResponse> exercises;
    private LocalDateTime createdAt;
}
