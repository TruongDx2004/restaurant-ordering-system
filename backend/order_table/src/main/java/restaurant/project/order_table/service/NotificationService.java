package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;

public interface NotificationService {

	/**
	 * Create a new notification
	 *
	 * @param notification notification data
	 * @return created notification
	 */
	NotificationEntity createNotification(NotificationEntity notification);

	/**
	 * Get notification by ID
	 *
	 * @param id notification ID
	 * @return notification entity
	 */
	NotificationEntity getNotificationById(Long id);

	/**
	 * Get all notifications
	 *
	 * @return list of all notifications
	 */
	List<NotificationEntity> getAllNotifications();

	/**
	 * Update notification
	 *
	 * @param id           notification ID
	 * @param notification updated notification data
	 * @return updated notification
	 */
	NotificationEntity updateNotification(Long id, NotificationEntity notification);

	/**
	 * Delete notification
	 *
	 * @param id notification ID
	 */
	void deleteNotification(Long id);

	/**
	 * Get notifications by recipient
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 * @return list of notifications
	 */
	List<NotificationEntity> getNotificationsByRecipient(RecipientType recipientType, Long recipientId);

	/**
	 * Get unread notifications by recipient
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 * @return list of unread notifications
	 */
	List<NotificationEntity> getUnreadNotificationsByRecipient(RecipientType recipientType, Long recipientId);

	/**
	 * Get notifications by recipient ordered by date
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 * @return list of notifications ordered by date descending
	 */
	List<NotificationEntity> getNotificationsByRecipientOrderedByDate(RecipientType recipientType, Long recipientId);

	/**
	 * Mark notification as read
	 *
	 * @param id notification ID
	 * @return updated notification
	 */
	NotificationEntity markAsRead(Long id);

	/**
	 * Mark all notifications as read for recipient
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 */
	void markAllAsReadForRecipient(RecipientType recipientType, Long recipientId);

	/**
	 * Count unread notifications for recipient
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 * @return count of unread notifications
	 */
	Long countUnreadNotifications(RecipientType recipientType, Long recipientId);

	/**
	 * Send notification to recipient
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID
	 * @param title         notification title
	 * @param message       notification message
	 * @return created notification
	 */
	NotificationEntity sendNotification(RecipientType recipientType, Long recipientId, String title, String message);

	/**
	 * Create notification and broadcast via WebSocket (tương đương createAndSend
	 * trong JS).
	 * Dùng recipientId = 0L cho broadcast toàn bộ USER.
	 *
	 * @param recipientType recipient type
	 * @param recipientId   recipient ID (0L = broadcast)
	 * @param title         notification title
	 * @param message       notification message
	 * @param type          WebSocket event type (e.g. "NEW_ORDER",
	 *                      "PAYMENT_SUCCESS")
	 * @param data          extra data gửi kèm qua WebSocket
	 * @return created notification
	 */
	NotificationEntity createAndSend(RecipientType recipientType, Long recipientId,
			String title, String message,
			String type, Object data);
}
