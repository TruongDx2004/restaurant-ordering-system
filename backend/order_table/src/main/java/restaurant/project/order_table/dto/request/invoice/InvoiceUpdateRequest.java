package restaurant.project.order_table.dto.request.invoice;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.InvoiceStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceUpdateRequest {

    @NotNull(message = "Table ID is required")
    private Long tableId;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "Status is required")
    private InvoiceStatus status;
}
