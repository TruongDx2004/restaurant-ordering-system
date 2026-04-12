package restaurant.project.order_table.dto.response.notification;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.RecipientType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private RecipientType recipientType;
    private Long recipientId;
    private String title;
    private String message;
    private String type;
    private String data;
    private Boolean read;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
}
