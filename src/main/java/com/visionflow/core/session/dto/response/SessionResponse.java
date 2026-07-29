package com.visionflow.core.session.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.PerformanceRating;
import com.visionflow.core.session.enums.SessionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SessionResponse {
    private Long id;
    private Long therapyPlanId;
    private String therapyPlanTitle;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Integer sessionNumber;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private SessionStatus status;
    private PerformanceRating overallPerformance;
    private String sessionNotes;
    private String doctorObservations;
    private String patientFeedback;
    private List<ExerciseLogResponse> exerciseLogs;
    private LocalDateTime createdAt;
}
