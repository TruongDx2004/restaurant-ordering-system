package restaurant.project.order_table.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import restaurant.project.order_table.entity.enums.MessageSender;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * Send notification to all connected clients
	 */
	public void sendGlobalNotification(String type, String content, Object data) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type(type)
				.sender(MessageSender.SYSTEM)
				.content(content)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/notifications", message);
		log.info("Sent global notification: {}", content);
	}

	/**
	 * Send new order notification to staff
	 */
	public void sendNewOrderNotification(Long invoiceId, Long tableId, String content) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("NEW_ORDER")
				.sender(MessageSender.SYSTEM)
				.content(content)
				.invoiceId(invoiceId)
				.tableId(tableId)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/orders", message);
		log.info("Sent new order notification for table {}", tableId);
	}

	/**
	 * Send table status update
	 */
	public void sendTableStatusUpdate(Long tableId, String status, Object data) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("TABLE_STATUS_UPDATE")
				.sender(MessageSender.SYSTEM)
				.content("Table " + tableId + " status: " + status)
				.tableId(tableId)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/table-status", message);
		log.info("Sent table status update for table {}", tableId);
	}

	/**
	 * Send message to specific table
	 */
	public void sendMessageToTable(Long tableId, MessageSender sender, String content) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("CHAT_MESSAGE")
				.sender(sender)
				.content(content)
				.tableId(tableId)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/chat/" + tableId, message);
		log.info("Sent message to table {}: {}", tableId, content);
	}

	/**
	 * Send message from customer – notifies employee inbox
	 */
	public void sendMessageFromCustomer(Long tableId, MessageSender sender, String content) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("CHAT_MESSAGE")
				.sender(sender)
				.content(content)
				.tableId(tableId)
				.timestamp(LocalDateTime.now())
				.build();

		// Employee inbox subscribes to /topic/employee/chat
		messagingTemplate.convertAndSend("/topic/employee/chat", message);
		log.info("Sent customer message from table {} to employee inbox", tableId);
	}

	/**
	 * Send notification to specific user
	 */
	public void sendNotificationToUser(String username, String type, String content, Object data) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type(type)
				.sender(MessageSender.SYSTEM)
				.content(content)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
		log.info("Sent notification to user {}: {}", username, content);
	}

	/**
	 * Broadcast payment notification
	 */
	public void sendPaymentNotification(Long invoiceId, Long tableId, String status) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("PAYMENT_STATUS")
				.sender(MessageSender.SYSTEM)
				.content("Payment " + status + " for table " + tableId)
				.invoiceId(invoiceId)
				.tableId(tableId)
				.data(status)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/payments", message);
		log.info("Sent payment notification for invoice {}", invoiceId);
	}

	/**
	 * Send invoice item status update
	 */
	public void sendInvoiceItemStatusUpdate(Long invoiceId, Long itemId, String status) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("ITEM_STATUS_UPDATE")
				.sender(MessageSender.SYSTEM)
				.content("Item " + itemId + " in order " + invoiceId + " status: " + status)
				.invoiceId(invoiceId)
				.data(itemId)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/orders/status", message);
		log.info("Sent status update for item {} in order {}", itemId, invoiceId);
	}
}
