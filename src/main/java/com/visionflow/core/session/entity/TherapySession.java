package com.visionflow.core.session.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.session.enums.PerformanceRating;
import com.visionflow.core.session.enums.SessionStatus;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Design decisions:
 * - ManyToOne TherapyPlan + Patient + Doctor: a session belongs to one plan,
 * conducted by one doctor for one patient. Patient/Doctor are denormalized
 * here for query efficiency — avoids joining through therapy_plans on every read.
 * - sessionNumber: sequential number within a plan (1, 2, 3...) for progress tracking.
 * - durationMinutes: actual duration recorded at session end, may differ from planned.
 * - overallPerformance: doctor's holistic rating of the session outcome.
 * - status lifecycle: SCHEDULED → IN_PROGRESS → COMPLETED. CANCELLED is terminal.
 * - OneToMany exerciseLogs with CascadeALL + orphanRemoval: logs are owned by session.
 */
@Entity
@Table(
        name = "therapy_sessions",
        indexes = {
                @Index(name = "idx_sessions_plan_id", columnList = "therapy_plan_id"),
                @Index(name = "idx_sessions_patient_id", columnList = "patient_id"),
                @Index(name = "idx_sessions_doctor_id", columnList = "doctor_id"),
                @Index(name = "idx_sessions_status", columnList = "status"),
                @Index(name = "idx_sessions_date", columnList = "session_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TherapySession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "therapy_sessions_seq")
    @SequenceGenerator(name = "therapy_sessions_seq", sequenceName = "therapy_sessions_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "therapy_plan_id", nullable = false)
    private TherapyPlan therapyPlan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "session_number", nullable = false)
    private Integer sessionNumber;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_performance", length = 20)
    private PerformanceRating overallPerformance;

    @Column(name = "session_notes", columnDefinition = "TEXT")
    private String sessionNotes;

    @Column(name = "doctor_observations", columnDefinition = "TEXT")
    private String doctorObservations;

    @Column(name = "patient_feedback", columnDefinition = "TEXT")
    private String patientFeedback;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExerciseLog> exerciseLogs = new ArrayList<>();
}
