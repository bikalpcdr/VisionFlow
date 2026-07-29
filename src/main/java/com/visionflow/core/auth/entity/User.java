package com.visionflow.core.auth.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.enums.Role;
import com.visionflow.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Design decisions:
 * - Long PK: users are internal, sequential, never exposed in URLs (UUIDs used in DTOs if needed).
 * - email is the unique business identifier and login credential.
 * - role stored as STRING for readability in DB and safe enum evolution.
 * - password is never included in any response DTO — enforced at mapper level.
 * - UserDetails implemented here to keep Spring Security integration clean
 *   without a separate adapter class.
 */
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Column(name = "phone", length = 20)
    private String phone;
}
