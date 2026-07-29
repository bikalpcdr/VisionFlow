package com.visionflow.core.session.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.enums.CompletionStatus;
import com.visionflow.core.therapyplan.entity.TherapyExercise;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Design decisions:
 * - ManyToOne TherapySession + TherapyExercise: each log records one exercise
 * within one session. TherapyExercise is the template; ExerciseLog is the execution record.
 * - actualDurationMinutes: may differ from the planned durationMinutes on the exercise.
 * - difficultyRating (1–5): patient-reported difficulty, useful for plan adjustment.
 * - completionStatus: granular tracking — PARTIAL means started but not finished.
 * - patientNotes / doctorNotes: separate fields to preserve both perspectives.
 */
@Entity
@Table(
        name = "exercise_logs",
        indexes = {
                @Index(name = "idx_exercise_logs_session_id", columnList = "session_id"),
                @Index(name = "idx_exercise_logs_exercise_id", columnList = "exercise_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ExerciseLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_logs_seq")
    @SequenceGenerator(name = "exercise_logs_seq", sequenceName = "exercise_logs_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private TherapySession session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private TherapyExercise exercise;

    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status", nullable = false, length = 20)
    private CompletionStatus completionStatus;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "difficulty_rating")
    private Integer difficultyRating;

    @Column(name = "patient_notes", columnDefinition = "TEXT")
    private String patientNotes;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;
}
