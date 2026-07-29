package com.visionflow.core.notification.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.notification.dto.response.NotificationResponse;
import com.visionflow.core.notification.enums.NotificationType;
import com.visionflow.core.notification.enums.ReferenceType;

import java.util.List;

public interface NotificationService {

    void send(Long recipientId, NotificationType type, String title, String message,
              ReferenceType referenceType, Long referenceId);

    NotificationResponse markAsRead(Long id, Long recipientId);

    void markAllAsRead(Long recipientId);

    void delete(Long id, Long recipientId);

    List<NotificationResponse> getMyNotifications(Long recipientId);

    List<NotificationResponse> getMyUnread(Long recipientId);

    long countUnread(Long recipientId);
}
