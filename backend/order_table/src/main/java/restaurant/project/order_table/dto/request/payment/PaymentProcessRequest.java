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

    @NotNull(message = "Invoice ID không được để trống")
    private Long invoiceId;

    @NotNull(message = "Payment method không được để trống")
    private PaymentMethod method;

    @NotNull(message = "Amount không được để trống")
    private BigDecimal amount;
}
