package restaurant.project.order_table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.NotificationEntity;
import restaurant.project.order_table.entity.enums.RecipientType;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	List<NotificationEntity> findByRecipientTypeAndRecipientId(RecipientType recipientType, Long recipientId);

	List<NotificationEntity> findByRecipientTypeAndRecipientIdAndRead(RecipientType recipientType, Long recipientId,
			Boolean read);

	List<NotificationEntity> findByRecipientTypeOrderByCreatedAtDesc(RecipientType recipientType);

	Long countByRecipientTypeAndRecipientIdAndRead(RecipientType recipientType, Long recipientId, Boolean read);
}
