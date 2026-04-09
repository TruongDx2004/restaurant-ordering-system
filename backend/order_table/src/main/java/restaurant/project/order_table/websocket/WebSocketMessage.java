package restaurant.project.order_table.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.MessageSender;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketMessage {

    private String type;           // MESSAGE, ORDER, TABLE_STATUS, NOTIFICATION, CHAT
    private MessageSender sender;
    private String content;        // Message content
    private Object data;           // Additional data (order, table info, etc.)
    private Long tableId;          // Table ID if relevant
    private Long invoiceId;         // Invoice ID if relevant
    private LocalDateTime timestamp;

    public WebSocketMessage(String type, MessageSender sender, String content) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
