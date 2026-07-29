package com.visionflow.core.appointment.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.enums.AppointmentStatus;
import com.visionflow.core.appointment.enums.AppointmentType;
import com.visionflow.core.assessment.entity.Assessment;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.patient.entity.Patient;
import com.visionflow.core.therapyplan.entity.TherapyPlan;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Design decisions:
 * - ManyToOne Patient + Doctor: an appointment is always between one patient and one doctor.
 * - appointmentDate + durationMinutes: stores the scheduled slot; actual end time is derived.
 * - assessment/therapyPlan are nullable FKs: an appointment may be standalone (first visit)
 * or linked to an existing assessment or plan (follow-up, review).
 * - cancellationReason: captured when status transitions to CANCELLED or NO_SHOW.
 * - status lifecycle: REQUESTED → CONFIRMED → IN_PROGRESS → COMPLETED.
 * CANCELLED and NO_SHOW are terminal from any non-COMPLETED state.
 * - patientNotes: reason for visit from patient perspective.
 * - doctorNotes: post-appointment clinical notes.
 */
@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_appointments_patient_id", columnList = "patient_id"),
                @Index(name = "idx_appointments_doctor_id", columnList = "doctor_id"),
                @Index(name = "idx_appointments_status", columnList = "status"),
                @Index(name = "idx_appointments_date", columnList = "appointment_date"),
                @Index(name = "idx_appointments_type", columnList = "appointment_type")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointments_seq")
    @SequenceGenerator(name = "appointments_seq", sequenceName = "appointments_id_seq", allocationSize = 1)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapy_plan_id")
    private TherapyPlan therapyPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false, length = 30)
    private AppointmentType appointmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "patient_notes", columnDefinition = "TEXT")
    private String patientNotes;

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    private String doctorNotes;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;
}
