package restaurant.project.order_table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	List<NotificationEntity> findByRecipientTypeAndRecipientId(RecipientType recipientType, Long recipientId);

	List<NotificationEntity> findByRecipientTypeAndRecipientIdAndRead(RecipientType recipientType, Long recipientId,
			Boolean read);

	List<NotificationEntity> findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(RecipientType recipientType,
			Long recipientId);

	@org.springframework.data.jpa.repository.Query("SELECT n FROM NotificationEntity n WHERE n.recipientType = :recipientType AND (n.recipientId = :recipientId OR n.recipientId = 0) ORDER BY n.createdAt DESC")
	List<NotificationEntity> findByRecipientTypeAndRecipientIdOrBroadcastOrderByCreatedAtDesc(
			@org.springframework.data.repository.query.Param("recipientType") RecipientType recipientType,
			@org.springframework.data.repository.query.Param("recipientId") Long recipientId);

	List<NotificationEntity> findByRecipientTypeOrderByCreatedAtDesc(RecipientType recipientType);

	Long countByRecipientTypeAndRecipientIdAndRead(RecipientType recipientType, Long recipientId, Boolean read);
}
