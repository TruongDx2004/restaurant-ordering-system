package restaurant.project.order_table.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.enums.MessageSender;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebSocketService {

	private final SimpMessagingTemplate messagingTemplate;

	public void sendGlobalNotification(String type, String content, Object data) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type(type)
				.sender(MessageSender.SYSTEM)
				.content(content)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/notifications", message);
	}

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
	}

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
	}

	public void sendMessageToTable(Long tableId, MessageSender sender, String content) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("CHAT_MESSAGE")
				.sender(sender)
				.content(content)
				.tableId(tableId)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/chat/" + tableId, message);
	}

	public void sendMessageFromCustomer(Long tableId, MessageSender sender, String content) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type("CHAT_MESSAGE")
				.sender(sender)
				.content(content)
				.tableId(tableId)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSend("/topic/employee/chat", message);
	}

	public void sendNotificationToUser(String username, String type, String content, Object data) {
		WebSocketMessage message = WebSocketMessage.builder()
				.type(type)
				.sender(MessageSender.SYSTEM)
				.content(content)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();

		messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
	}

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
	}

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
	}
}
