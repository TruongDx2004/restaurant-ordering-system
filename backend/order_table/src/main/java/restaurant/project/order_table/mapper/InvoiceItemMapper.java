package restaurant.project.order_table.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.invoiceitem.InvoiceItemCreateRequest;
import restaurant.project.order_table.dto.request.invoiceitem.InvoiceItemUpdateRequest;
import restaurant.project.order_table.dto.response.invoiceitem.InvoiceItemResponse;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;
import restaurant.project.order_table.service.DishService;
import restaurant.project.order_table.service.InvoiceService;

@Component
@RequiredArgsConstructor
public class InvoiceItemMapper {

    private final InvoiceService invoiceService;
    private final DishService dishService;
    private final DishMapper dishMapper;

    public InvoiceItemEntity toEntity(InvoiceItemCreateRequest request) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(request.getInvoiceId());
        DishEntity dish = dishService.getDishById(request.getDishId());
        
        return InvoiceItemEntity.builder()
                .invoice(invoice)
                .dish(dish)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getTotalPrice())
                .status(request.getStatus())
                .note(request.getNote())
                .build();
    }

    public InvoiceItemEntity toEntity(InvoiceItemUpdateRequest request) {
        DishEntity dish = dishService.getDishById(request.getDishId());
        
        return InvoiceItemEntity.builder()
                .dish(dish)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .status(request.getStatus() != null ? InvoiceItemStatus.valueOf(request.getStatus()) : null)
                .note(request.getNote())
                .build();
    }

    public InvoiceItemResponse toResponse(InvoiceItemEntity entity) {
        return InvoiceItemResponse.builder()
                .id(entity.getId())
                .dish(dishMapper.toResponse(entity.getDish()))
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus().name())
                .note(entity.getNote())
                .build();
    }

    public List<InvoiceItemResponse> toResponseList(List<InvoiceItemEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
