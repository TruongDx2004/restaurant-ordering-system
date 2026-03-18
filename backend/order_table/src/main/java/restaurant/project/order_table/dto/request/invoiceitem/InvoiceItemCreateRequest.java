package restaurant.project.order_table.dto.request.invoiceitem;

import java.math.BigDecimal;

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

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Dish ID is required")
    private Long dishId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    private InvoiceItemStatus status; // WAITING, PREPARING, SERVED, CANCELLED
    private String note; // Ghi chú từ khách
}
