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

    @NotBlank(message = "Content không được để trống")
    private String content;

    @NotNull(message = "Message type không được để trống")
    private MessageType messageType;

    @NotNull(message = "Sender không được để trống")
    private MessageSender sender;

    private Long invoiceId;

    private Long tableId;
}
