package com.visionflow.core.therapyplan.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.entity.Assessment;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.therapyplan.enums.TherapyPlanStatus;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Design decisions:
 * - OneToMany exercises with CascadeType.ALL + orphanRemoval=true:
 * exercises are owned by the plan — they have no independent lifecycle.
 * Deleting a plan deletes its exercises. Removing from the list deletes the exercise.
 * - Assessment is nullable FK: a plan can be created without a prior assessment
 * (e.g. follow-up plans, maintenance plans).
 * - startDate/endDate as LocalDate: therapy plans span days/weeks, time-of-day is irrelevant.
 * - exercises initialized as empty ArrayList to prevent NPE on add operations.
 */
@Entity
@Table(
        name = "therapy_plans",
        indexes = {
                @Index(name = "idx_therapy_plans_patient_id", columnList = "patient_id"),
                @Index(name = "idx_therapy_plans_doctor_id", columnList = "doctor_id"),
                @Index(name = "idx_therapy_plans_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TherapyPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "therapy_plans_seq")
    @SequenceGenerator(name = "therapy_plans_seq", sequenceName = "therapy_plans_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TherapyPlanStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "goals", columnDefinition = "TEXT")
    private String goals;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "therapyPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TherapyExercise> exercises = new ArrayList<>();
}
