package com.visionflow.core.notification.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndRecipientIdAndDeletedFalse(Long id, Long recipientId);

    List<Notification> findByRecipientIdAndDeletedFalseOrderByCreatedAtDesc(Long recipientId);

    List<Notification> findByRecipientIdAndReadFalseAndDeletedFalseOrderByCreatedAtDesc(Long recipientId);

    long countByRecipientIdAndReadFalseAndDeletedFalse(Long recipientId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true, n.readAt = CURRENT_TIMESTAMP WHERE n.recipient.id = :recipientId AND n.read = false AND n.deleted = false")
    void markAllAsRead(@Param("recipientId") Long recipientId);
}
