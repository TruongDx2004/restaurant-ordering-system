package restaurant.project.order_table.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned when a new online payment is initiated.
 * The frontend should redirect the user to {@code paymentUrl}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlinePaymentInitResponse {

	/** Internal payment record ID (use to track status) */
	private Long paymentId;

	/** URL the frontend must redirect (or open) for the customer to pay */
	private String paymentUrl;

	/** Gateway-side order ID for reference (MoMo orderId / VNPay TxnRef) */
	private String gatewayOrderId;
}