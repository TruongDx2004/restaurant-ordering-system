package restaurant.project.order_table.dto.request.invoiceitem;

import java.math.BigDecimal;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemCreateRequest {

    @NotNull(message = "Invoice ID không được để trống")
    private Long invoiceId;

    @NotNull(message = "Dish ID không được để trống")
    private Long dishId;

    @NotNull(message = "Quantity không được để trống")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price không được để trống")
    private BigDecimal unitPrice;

    @NotNull(message = "Total price không được để trống")
    private BigDecimal totalPrice;

    @Builder.Default
    @NotNull(message = "Status không được để trống")
    @Enumerated(EnumType.STRING)
    private InvoiceItemStatus status = InvoiceItemStatus.WAITING; // WAITING, PREPARING, SERVED, CANCELLED
    
    private String note; // Ghi chú từ khách
}
