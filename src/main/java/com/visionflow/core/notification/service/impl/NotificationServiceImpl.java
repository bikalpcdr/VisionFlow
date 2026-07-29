package com.visionflow.core.notification.service.impl;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.auth.service.UserService;
import com.visionflow.core.notification.dto.response.NotificationResponse;
import com.visionflow.core.notification.entity.Notification;
import com.visionflow.core.notification.enums.NotificationType;
import com.visionflow.core.notification.enums.ReferenceType;
import com.visionflow.core.notification.repo.NotificationRepository;
import com.visionflow.core.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void send(Long recipientId, NotificationType type, String title, String message,
                     ReferenceType referenceType, Long referenceId) {
        Notification notification = new Notification();
        notification.setRecipient(userService.getEntityById(recipientId));
        notification.setNotificationType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setReferenceType(referenceType);
        notification.setReferenceId(referenceId);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long id, Long recipientId) {
        Notification notification = notificationRepository
                .findByIdAndRecipientIdAndDeletedFalse(id, recipientId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
        return toResponse(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long recipientId) {
        notificationRepository.markAllAsRead(recipientId);
    }

    @Override
    @Transactional
    public void delete(Long id, Long recipientId) {
        Notification notification = notificationRepository
                .findByIdAndRecipientIdAndDeletedFalse(id, recipientId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdAndDeletedFalseOrderByCreatedAtDesc(recipientId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyUnread(Long recipientId) {
        return notificationRepository.findByRecipientIdAndReadFalseAndDeletedFalseOrderByCreatedAtDesc(recipientId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long recipientId) {
        return notificationRepository.countByRecipientIdAndReadFalseAndDeletedFalse(recipientId);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getNotificationType(),
                n.getReferenceType(),
                n.getReferenceId(),
                n.getTitle(),
                n.getMessage(),
                n.isRead(),
                n.getReadAt(),
                n.getCreatedAt()
        );
    }
}
