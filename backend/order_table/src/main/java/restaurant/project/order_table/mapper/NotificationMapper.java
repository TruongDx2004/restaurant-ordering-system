package restaurant.project.order_table.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import restaurant.project.order_table.dto.request.notification.NotificationCreateRequest;
import restaurant.project.order_table.dto.response.notification.NotificationResponse;
import restaurant.project.order_table.entity.NotificationEntity;

@Component
public class NotificationMapper {

    public NotificationEntity toEntity(NotificationCreateRequest request) {
        return NotificationEntity.builder()
                .recipientType(request.getRecipientType())
                .recipientId(request.getRecipientId())
                .title(request.getTitle())
                .message(request.getMessage())
                .read(false)
                .build();
    }

    public NotificationResponse toResponse(NotificationEntity entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .recipientType(entity.getRecipientType())
                .recipientId(entity.getRecipientId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .read(entity.getRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public List<NotificationResponse> toResponseList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
