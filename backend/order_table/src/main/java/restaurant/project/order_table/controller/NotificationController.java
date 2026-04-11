package restaurant.project.order_table.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.notification.NotificationResponse;
import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;
import restaurant.project.order_table.mapper.NotificationMapper;
import restaurant.project.order_table.service.NotificationService;

/**
 * GET /api/notifications – tất cả thông báo (STAFF)
 * GET /api/notifications/recipient/ordered – thông báo theo recipient, mới nhất
 * trước (STAFF)
 * GET /api/notifications/recipient/unread-count – đếm chưa đọc (STAFF)
 * PATCH /api/notifications/{id}/mark-read – đánh dấu đã đọc (STAFF)
 * PATCH /api/notifications/recipient/mark-all-read – đánh dấu tất cả đã đọc
 * (STAFF)
 * DELETE /api/notifications/{id} – xóa thông báo (STAFF)
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	private final NotificationMapper notificationMapper;

	@GetMapping
	public ApiResponse<List<NotificationResponse>> getAllNotifications() {
		return ApiResponse.success(
				notificationMapper.toResponseList(notificationService.getAllNotifications()),
				"Lấy danh sách thông báo thành công");
	}

	@GetMapping("/recipient/ordered")
	public ApiResponse<List<NotificationResponse>> getByRecipientOrdered(
			@RequestParam(defaultValue = "USER") RecipientType recipientType,
			@RequestParam(required = false) Long recipientId) {
		List<NotificationEntity> list = notificationService.getNotificationsByRecipientOrderedByDate(recipientType, recipientId);
		return ApiResponse.success(notificationMapper.toResponseList(list), "Lấy thông báo thành công");
	}

	@GetMapping("/recipient/unread-count")
	public ApiResponse<Long> countUnread(
			@RequestParam(defaultValue = "ALL") RecipientType recipientType,
			@RequestParam(required = false, defaultValue = "0") Long recipientId) {
		return ApiResponse.success(
				notificationService.countUnreadNotifications(recipientType, recipientId),
				"Đếm thông báo thành công");
	}

	@PatchMapping("/{id}/mark-read")
	public ApiResponse<NotificationResponse> markAsRead(@PathVariable Long id) {
		return ApiResponse.success(
				notificationMapper.toResponse(notificationService.markAsRead(id)),
				"Đánh dấu đã đọc thành công");
	}

	@PatchMapping("/recipient/mark-all-read")
	public ApiResponse<Void> markAllAsRead(
			@RequestParam(defaultValue = "ALL") RecipientType recipientType,
			@RequestParam(required = false, defaultValue = "0") Long recipientId) {
		notificationService.markAllAsReadForRecipient(recipientType, recipientId);
		return ApiResponse.success(null, "Tất cả thông báo đã được đánh dấu đọc");
	}

	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteNotification(@PathVariable Long id) {
		notificationService.deleteNotification(id);
		return ApiResponse.success(null, "Xóa thông báo thành công");
	}
}
