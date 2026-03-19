package restaurant.project.order_table.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.notification.NotificationCreateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.notification.NotificationResponse;
import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;
import restaurant.project.order_table.mapper.NotificationMapper;
import restaurant.project.order_table.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    /**
     * Create a new notification
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NotificationResponse> createNotification(@Valid @RequestBody NotificationCreateRequest request) {
        NotificationEntity entity = notificationMapper.toEntity(request);
        NotificationEntity created = notificationService.createNotification(entity);
        return ApiResponse.success(notificationMapper.toResponse(created), "Notification created successfully");
    }

    /**
     * Get notification by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotificationById(@PathVariable Long id) {
        NotificationEntity notification = notificationService.getNotificationById(id);
        return ApiResponse.success(notificationMapper.toResponse(notification), "Notification retrieved successfully");
    }

    /**
     * Get all notifications
     */
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationMapper.toResponseList(notificationService.getAllNotifications());
        return ApiResponse.success(notifications, "Notifications retrieved successfully");
    }

    /**
     * Update notification
     */
    @PutMapping("/{id}")
    public ApiResponse<NotificationResponse> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationCreateRequest request) {
        NotificationEntity entity = notificationMapper.toEntity(request);
        NotificationEntity updated = notificationService.updateNotification(id, entity);
        return ApiResponse.success(notificationMapper.toResponse(updated), "Notification updated successfully");
    }

    /**
     * Delete notification
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ApiResponse.success(null, "Notification deleted successfully");
    }

    /**
     * Get notifications by recipient
     */
    @GetMapping("/recipient")
    public ApiResponse<List<NotificationResponse>> getNotificationsByRecipient(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId) {
        List<NotificationResponse> notifications = notificationMapper.toResponseList(
                notificationService.getNotificationsByRecipient(recipientType, recipientId));
        return ApiResponse.success(notifications, "Notifications retrieved successfully");
    }

    /**
     * Get unread notifications by recipient
     */
    @GetMapping("/recipient/unread")
    public ApiResponse<List<NotificationResponse>> getUnreadNotificationsByRecipient(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId) {
        List<NotificationResponse> notifications = notificationMapper.toResponseList(
                notificationService.getUnreadNotificationsByRecipient(recipientType, recipientId));
        return ApiResponse.success(notifications, "Unread notifications retrieved successfully");
    }

    /**
     * Get notifications by recipient ordered by date
     */
    @GetMapping("/recipient/ordered")
    public ApiResponse<List<NotificationResponse>> getNotificationsByRecipientOrderedByDate(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId) {
        List<NotificationResponse> notifications = notificationMapper.toResponseList(
                notificationService.getNotificationsByRecipientOrderedByDate(recipientType, recipientId));
        return ApiResponse.success(notifications, "Notifications retrieved successfully");
    }

    /**
     * Mark notification as read
     */
    @PatchMapping("/{id}/mark-read")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationEntity updated = notificationService.markAsRead(id);
        return ApiResponse.success(notificationMapper.toResponse(updated), "Notification marked as read");
    }

    /**
     * Mark all notifications as read for recipient
     */
    @PatchMapping("/recipient/mark-all-read")
    public ApiResponse<Void> markAllAsReadForRecipient(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId) {
        notificationService.markAllAsReadForRecipient(recipientType, recipientId);
        return ApiResponse.success(null, "All notifications marked as read");
    }

    /**
     * Count unread notifications for recipient
     */
    @GetMapping("/recipient/unread-count")
    public ApiResponse<Long> countUnreadNotifications(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId) {
        Long count = notificationService.countUnreadNotifications(recipientType, recipientId);
        return ApiResponse.success(count, "Unread notification count retrieved successfully");
    }

    /**
     * Send notification to recipient
     */
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NotificationResponse> sendNotification(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId,
            @RequestParam String title,
            @RequestParam String message) {
        NotificationEntity created = notificationService.sendNotification(recipientType, recipientId, title, message);
        return ApiResponse.success(notificationMapper.toResponse(created), "Notification sent successfully");
    }
}
