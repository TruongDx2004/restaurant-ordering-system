package restaurant.project.order_table.dto.request.invoiceitem;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemUpdateRequest {

    @NotNull(message = "Dish ID không được để trống")
    private Long dishId;

    @NotNull(message = "Quantity không được để trống")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price không được để trống")
    private BigDecimal unitPrice;
    
    private String status; // WAITING, PREPARING, SERVED, CANCELLED
    private String note; // Ghi chú từ khách
}
