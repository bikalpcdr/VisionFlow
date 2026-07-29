package com.visionflow.core.assessment.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.assessment.enums.AssessmentStatus;
import com.visionflow.core.assessment.enums.AssessmentType;
import com.visionflow.core.assessment.enums.EyeConditionSeverity;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Design decisions:
 * - ManyToOne Patient + Doctor: one patient has many assessments over time;
 * each assessment is performed by one doctor.
 * - Visual acuity stored as String (e.g. "6/6", "20/20", "6/12") — clinical
 * notation is not a numeric value, string is the correct type.
 * - Dedicated columns per eye for all measurements — enables SQL-level filtering,
 * aggregation, and trend analysis without parsing JSON.
 * - coverTestResult, npcResult, stereopsisResult stored as TEXT — these are
 * free-form clinical findings that vary by technique and equipment.
 * - severity stored as enum — drives therapy plan recommendation logic.
 * - assessmentDate separate from createdAt — a record can be entered after the
 * actual clinical visit date.
 * - recommendations: TEXT — doctor's free-form clinical recommendations.
 * - status lifecycle: DRAFT → COMPLETED → REVIEWED. CANCELLED is terminal.
 */
@Entity
@Table(
        name = "assessments",
        indexes = {
                @Index(name = "idx_assessments_patient_id", columnList = "patient_id"),
                @Index(name = "idx_assessments_doctor_id", columnList = "doctor_id"),
                @Index(name = "idx_assessments_type", columnList = "assessment_type"),
                @Index(name = "idx_assessments_status", columnList = "status"),
                @Index(name = "idx_assessments_date", columnList = "assessment_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Assessment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessments_seq")
    @SequenceGenerator(name = "assessments_seq", sequenceName = "assessments_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", nullable = false, length = 50)
    private AssessmentType assessmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AssessmentStatus status;

    @Column(name = "assessment_date", nullable = false)
    private LocalDateTime assessmentDate;

    // ===== Visual Acuity =====
    @Column(name = "visual_acuity_left", length = 20)
    private String visualAcuityLeft;

    @Column(name = "visual_acuity_right", length = 20)
    private String visualAcuityRight;

    @Column(name = "visual_acuity_binocular", length = 20)
    private String visualAcuityBinocular;

    // ===== Refraction =====
    @Column(name = "sphere_left")
    private Double sphereLeft;

    @Column(name = "sphere_right")
    private Double sphereRight;

    @Column(name = "cylinder_left")
    private Double cylinderLeft;

    @Column(name = "cylinder_right")
    private Double cylinderRight;

    @Column(name = "axis_left")
    private Integer axisLeft;

    @Column(name = "axis_right")
    private Integer axisRight;

    // ===== Binocular Vision =====
    @Column(name = "cover_test_result", columnDefinition = "TEXT")
    private String coverTestResult;

    @Column(name = "npc_result", length = 100)
    private String npcResult;

    @Column(name = "stereopsis_result", length = 100)
    private String stereopsisResult;

    // ===== Condition-Specific =====
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 20)
    private EyeConditionSeverity severity;

    @Column(name = "affected_eye", length = 20)
    private String affectedEye;

    @Column(name = "iop_left")
    private Double iopLeft;

    @Column(name = "iop_right")
    private Double iopRight;

    // ===== Clinical Notes =====
    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(name = "clinical_findings", columnDefinition = "TEXT")
    private String clinicalFindings;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;
}
