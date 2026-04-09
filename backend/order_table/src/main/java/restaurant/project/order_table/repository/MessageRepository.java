package restaurant.project.order_table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByInvoiceId(Long invoiceId);

    List<MessageEntity> findByTableId(Long tableId);

    List<MessageEntity> findByMessageType(MessageType messageType);

    List<MessageEntity> findBySender(MessageSender sender);

    List<MessageEntity> findByTableIdOrderByCreatedAtDesc(Long tableId);

    /** Lấy tin nhắn mới nhất của bàn (dùng cho getConversations) */
    Optional<MessageEntity> findTopByTableIdOrderByCreatedAtDesc(Long tableId);
}
