package restaurant.project.order_table.dto.response.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private String transactionCode;
}
