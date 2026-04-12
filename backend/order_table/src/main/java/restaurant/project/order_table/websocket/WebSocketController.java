package restaurant.project.order_table.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public WebSocketMessage sendMessage(@Payload WebSocketMessage message) {
        log.info("Received message: {}", message);
        return message;
    }

    @MessageMapping("/order")
    @SendTo("/topic/orders")
    public WebSocketMessage sendOrderNotification(@Payload WebSocketMessage message) {
        log.info("New order notification: {}", message);
        return message;
    }

    @MessageMapping("/table-status")
    @SendTo("/topic/table-status")
    public WebSocketMessage sendTableStatusUpdate(@Payload WebSocketMessage message) {
        log.info("Table status update: {}", message);
        return message;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public WebSocketMessage sendChatMessage(@Payload WebSocketMessage message) {
        log.info("Chat message: {}", message);
        return message;
    }

    @MessageMapping("/join")
    @SendTo("/topic/users")
    public WebSocketMessage userJoin(@Payload WebSocketMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // Add username to websocket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        log.info("User joined: {}", message.getSender());
        return message;
    }
}
