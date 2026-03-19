package restaurant.project.order_table.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketMessage {

    private String type;           // MESSAGE, ORDER, TABLE_STATUS, NOTIFICATION, CHAT
    private String sender;         // Username or user ID
    private String content;        // Message content
    private Object data;           // Additional data (order, table info, etc.)
    private Long tableId;          // Table ID if relevant
    private Long orderId;          // Order/Invoice ID if relevant
    private LocalDateTime timestamp;

    public WebSocketMessage(String type, String sender, String content) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
