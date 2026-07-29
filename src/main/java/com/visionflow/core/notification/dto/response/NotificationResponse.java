package com.visionflow.core.notification.dto.response;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.notification.enums.NotificationType;
import com.visionflow.core.notification.enums.ReferenceType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType notificationType,
        ReferenceType referenceType,
        Long referenceId,
        String title,
        String message,
        boolean read,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
}
