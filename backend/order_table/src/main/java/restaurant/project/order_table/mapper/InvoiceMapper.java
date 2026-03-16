package restaurant.project.order_table.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.invoice.InvoiceCreateRequest;
import restaurant.project.order_table.dto.request.invoice.InvoiceUpdateRequest;
import restaurant.project.order_table.dto.response.invoice.InvoiceResponse;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.service.TableService;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final TableService tableService;
    private final TableMapper tableMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceEntity toEntity(InvoiceCreateRequest request) {
        TableEntity table = tableService.getTableById(request.getTableId());
        
        return InvoiceEntity.builder()
                .table(table)
                .totalAmount(request.getTotalAmount())
                .status(request.getStatus())
                .build();
    }

    public InvoiceEntity toEntity(InvoiceUpdateRequest request) {
        TableEntity table = tableService.getTableById(request.getTableId());
        
        return InvoiceEntity.builder()
                .table(table)
                .totalAmount(request.getTotalAmount())
                .status(request.getStatus())
                .build();
    }

    public InvoiceResponse toResponse(InvoiceEntity entity) {
        return InvoiceResponse.builder()
                .id(entity.getId())
                .table(tableMapper.toResponse(entity.getTable()))
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .paidAt(entity.getPaidAt())
                .items(entity.getItems() != null 
                    ? entity.getItems().stream()
                        .map(invoiceItemMapper::toResponse)
                        .collect(Collectors.toList())
                    : Collections.emptyList())
                .build();
    }

    public List<InvoiceResponse> toResponseList(List<InvoiceEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
