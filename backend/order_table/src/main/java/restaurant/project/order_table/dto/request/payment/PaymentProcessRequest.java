package restaurant.project.order_table.dto.request.payment;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.PaymentMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessRequest {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}
