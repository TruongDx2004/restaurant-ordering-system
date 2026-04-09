package restaurant.project.order_table.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.TableStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {

    private Long id;
    private Integer tableNumber;
    private TableStatus status;
    private MessageSender sender;
    private String lastMessage;
    private LocalDateTime lastTime;
    @Builder.Default
    private Integer unreadCount = 0;
}
