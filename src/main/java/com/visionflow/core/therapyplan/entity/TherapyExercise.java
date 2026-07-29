package com.visionflow.core.therapyplan.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.therapyplan.enums.ExerciseType;
import com.visionflow.core.therapyplan.enums.FrequencyUnit;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Design decisions:
 * - Separate entity (not @Embeddable): exercises need their own audit trail,
 * independent update capability, and future progress tracking linkage.
 * - durationMinutes as Integer: duration is always in minutes for consistency.
 * - orderIndex: allows doctor to define the sequence of exercises in a plan.
 */
@Entity
@Table(
        name = "therapy_exercises",
        indexes = {
                @Index(name = "idx_therapy_exercises_plan_id", columnList = "therapy_plan_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TherapyExercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "therapy_exercises_seq")
    @SequenceGenerator(name = "therapy_exercises_seq", sequenceName = "therapy_exercises_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "therapy_plan_id", nullable = false)
    private TherapyPlan therapyPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false, length = 50)
    private ExerciseType exerciseType;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_unit", nullable = false, length = 20)
    private FrequencyUnit frequencyUnit;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "repetitions")
    private Integer repetitions;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;
}
