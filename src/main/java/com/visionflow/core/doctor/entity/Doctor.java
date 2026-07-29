package com.visionflow.core.doctor.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.entity.User;
import com.visionflow.core.doctor.enums.Specialization;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Design decisions:
 * - OneToOne with User: a Doctor IS a User with role=DOCTOR plus professional data.
 * Keeping them separate avoids a bloated users table and allows doctor-specific
 * queries without joining unrelated user columns.
 * - LAZY fetch on user: we rarely need the full User when working with Doctor data.
 * Fetch explicitly when needed via JOIN FETCH in queries.
 * - licenseNumber: unique per doctor, used for verification and audit purposes.
 * - yearsOfExperience: stored as integer, computed from practice start date if needed.
 */
@Entity
@Table(
        name = "doctors",
        indexes = {
                @Index(name = "idx_doctors_user_id", columnList = "user_id", unique = true),
                @Index(name = "idx_doctors_license_number", columnList = "license_number", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctors_seq")
    @SequenceGenerator(name = "doctors_seq", sequenceName = "doctors_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false, length = 50)
    private Specialization specialization;

    @Column(name = "license_number", nullable = false, unique = true, length = 100)
    private String licenseNumber;

    @Column(name = "qualification", nullable = false, length = 255)
    private String qualification;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "clinic_name", length = 200)
    private String clinicName;

    @Column(name = "clinic_address", length = 500)
    private String clinicAddress;
}
