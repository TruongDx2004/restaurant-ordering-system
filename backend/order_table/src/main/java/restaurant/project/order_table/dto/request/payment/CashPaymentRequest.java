package restaurant.project.order_table.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashPaymentRequest {

    @NotNull(message = "Invoice ID không được để trống")
    private Long invoiceId;

    @NotNull(message = "Table ID không được để trống")
    private Long tableId;

    @NotNull(message = "Amount không được để trống")
    @Positive(message = "Amount phai là số dương")
    private BigDecimal amount;
}
