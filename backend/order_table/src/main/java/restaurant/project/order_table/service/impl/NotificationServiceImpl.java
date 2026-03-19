package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.NotificationRepository;
import restaurant.project.order_table.service.NotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public NotificationEntity getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Notification not found with id: " + id));
    }

    @Override
    public List<NotificationEntity> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public NotificationEntity updateNotification(Long id, NotificationEntity notification) {
        NotificationEntity existingNotification = getNotificationById(id);

        existingNotification.setTitle(notification.getTitle());
        existingNotification.setMessage(notification.getMessage());
        existingNotification.setRecipientType(notification.getRecipientType());
        existingNotification.setRecipientId(notification.getRecipientId());
        existingNotification.setRead(notification.getRead());

        return notificationRepository.save(existingNotification);
    }

    @Override
    public void deleteNotification(Long id) {
        NotificationEntity notification = getNotificationById(id);
        notificationRepository.delete(notification);
    }

    @Override
    public List<NotificationEntity> getNotificationsByRecipient(RecipientType recipientType, Long recipientId) {
        return notificationRepository.findByRecipientTypeAndRecipientId(recipientType, recipientId);
    }

    @Override
    public List<NotificationEntity> getUnreadNotificationsByRecipient(RecipientType recipientType, Long recipientId) {
        return notificationRepository.findByRecipientTypeAndRecipientIdAndRead(recipientType, recipientId, false);
    }

    @Override
    public List<NotificationEntity> getNotificationsByRecipientOrderedByDate(RecipientType recipientType, Long recipientId) {
        return notificationRepository.findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(recipientType, recipientId);
    }

    @Override
    public NotificationEntity markAsRead(Long id) {
        NotificationEntity notification = getNotificationById(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void markAllAsReadForRecipient(RecipientType recipientType, Long recipientId) {
        List<NotificationEntity> notifications = getUnreadNotificationsByRecipient(recipientType, recipientId);
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public Long countUnreadNotifications(RecipientType recipientType, Long recipientId) {
        return notificationRepository.countByRecipientTypeAndRecipientIdAndRead(recipientType, recipientId, false);
    }

    @Override
    public NotificationEntity sendNotification(RecipientType recipientType, Long recipientId, String title, String message) {
        NotificationEntity notification = new NotificationEntity();
        notification.setRecipientType(recipientType);
        notification.setRecipientId(recipientId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);

        return notificationRepository.save(notification);
    }
}
