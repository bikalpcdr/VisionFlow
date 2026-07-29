package com.visionflow.shared.entity;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity inherited by all JPA entities.
 *
 * Design decisions:
 * - MappedSuperclass: fields are inlined into each child table (no join overhead).
 * - AuditingEntityListener: Spring Data auto-populates createdAt/updatedAt/createdBy/updatedBy.
 * - active + deleted: supports both logical deactivation and soft delete independently.
 *   active=false means the record is disabled but visible to admins.
 *   deleted=true means the record is soft-deleted and invisible to all normal queries.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
