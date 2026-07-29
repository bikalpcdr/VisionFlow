package com.visionflow.core.notification.controller;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.annotation.SuccessMessage;
import com.visionflow.config.security.UserPrincipal;
import com.visionflow.constant.MessageConstant;
import com.visionflow.core.notification.dto.response.NotificationResponse;
import com.visionflow.core.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "User notification management")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @SuccessMessage(entity = MessageConstant.NOTIFICATION, action = MessageConstant.FETCHED)
    @Operation(summary = "Get all my notifications")
    public List<NotificationResponse> getMyNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationService.getMyNotifications(principal.getId());
    }

    @GetMapping("/unread")
    @SuccessMessage(entity = MessageConstant.NOTIFICATION, action = MessageConstant.FETCHED)
    @Operation(summary = "Get my unread notifications")
    public List<NotificationResponse> getMyUnread(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationService.getMyUnread(principal.getId());
    }

    @GetMapping("/unread/count")
    @Operation(summary = "Get unread notification count")
    public ResponseEntity<Map<String, Long>> countUnread(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(Map.of("count", notificationService.countUnread(principal.getId())));
    }

    @PatchMapping("/{id}/read")
    @SuccessMessage(entity = MessageConstant.NOTIFICATION, action = MessageConstant.UPDATED)
    @Operation(summary = "Mark a notification as read")
    public NotificationResponse markAsRead(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        return notificationService.markAsRead(id, principal.getId());
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.delete(id, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
