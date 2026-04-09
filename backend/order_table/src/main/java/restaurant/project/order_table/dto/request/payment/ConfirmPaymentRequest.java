package restaurant.project.order_table.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentRequest {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    /** Mã giao dịch (tuỳ chọn, tự sinh nếu null) */
    private String transactionCode;
}
