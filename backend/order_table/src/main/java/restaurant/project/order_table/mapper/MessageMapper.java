package restaurant.project.order_table.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.message.MessageCreateRequest;
import restaurant.project.order_table.dto.response.message.MessageResponse;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.service.InvoiceService;
import restaurant.project.order_table.service.TableService;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final InvoiceService invoiceService;
    private final TableService tableService;

    public MessageEntity toEntity(MessageCreateRequest request) {
        MessageEntity.MessageEntityBuilder builder = MessageEntity.builder()
                .content(request.getContent())
                .messageType(request.getMessageType())
                .sender(request.getSender());

        if (request.getInvoiceId() != null) {
            InvoiceEntity invoice = invoiceService.getInvoiceById(request.getInvoiceId());
            builder.invoice(invoice);
        }

        if (request.getTableId() != null) {
            TableEntity table = tableService.getTableById(request.getTableId());
            builder.table(table);
        }

        return builder.build();
    }

    public MessageResponse toResponse(MessageEntity entity) {
        return MessageResponse.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .messageType(entity.getMessageType())
                .sender(entity.getSender())
                .invoiceId(entity.getInvoice() != null ? entity.getInvoice().getId() : null)
                .tableId(entity.getTable() != null ? entity.getTable().getId() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public List<MessageResponse> toResponseList(List<MessageEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
