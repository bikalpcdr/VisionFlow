package com.visionflow.core.patient.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.entity.User;
import com.visionflow.core.doctor.entity.Doctor;
import com.visionflow.core.patient.enums.BloodGroup;
import com.visionflow.core.patient.enums.Gender;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Design decisions:
 * - OneToOne with User: same pattern as Doctor — Patient IS a User with role=PATIENT
 * plus clinical profile data.
 * - ManyToOne with Doctor: a patient is assigned to one primary doctor.
 * This is nullable — a patient can be registered before doctor assignment.
 * - dateOfBirth stored as LocalDate (not age) — age is computed, DOB is immutable fact.
 * - emergencyContactName/Phone: critical for healthcare — stored directly on patient
 * rather than a separate table to avoid over-normalization for a simple 1-field relationship.
 * - medicalNotes: free-text clinical notes, TEXT column, no length restriction.
 */
@Entity
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patients_user_id", columnList = "user_id", unique = true),
                @Index(name = "idx_patients_doctor_id", columnList = "doctor_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patients_seq")
    @SequenceGenerator(name = "patients_seq", sequenceName = "patients_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor assignedDoctor;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 15)
    private BloodGroup bloodGroup;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "emergency_contact_name", length = 150)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "medical_notes", columnDefinition = "TEXT")
    private String medicalNotes;

    @Column(name = "allergies", length = 500)
    private String allergies;
}
