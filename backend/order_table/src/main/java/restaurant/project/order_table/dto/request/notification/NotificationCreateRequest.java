package restaurant.project.order_table.dto.request.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.RecipientType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequest {

    @NotNull(message = "Recipient type không được để trống")
    private RecipientType recipientType;

    @NotNull(message = "Recipient ID không được để trống")
    private Long recipientId;

    @NotBlank(message = "Title không được để trống")
    private String title;

    @NotBlank(message = "Message không được để trống")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    @NotBlank(message = "Type không được để trống")
    private String type;

    private String data;
}
