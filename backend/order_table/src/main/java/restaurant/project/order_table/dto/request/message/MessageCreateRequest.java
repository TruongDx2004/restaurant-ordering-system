package restaurant.project.order_table.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateRequest {

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Message type is required")
    private MessageType messageType;

    @NotNull(message = "Sender is required")
    private MessageSender sender;

    private Long invoiceId;

    private Long tableId;
}
