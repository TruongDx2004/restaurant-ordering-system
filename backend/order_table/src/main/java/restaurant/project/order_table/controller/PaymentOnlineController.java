package restaurant.project.order_table.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.payment.MomoPaymentInitRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.payment.OnlinePaymentInitResponse;
import restaurant.project.order_table.service.impl.PaymentOnlineServiceExtension;
import restaurant.project.order_table.util.MomoUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Online-payment endpoints — MoMo Pay Gate & VNPay QR.
 *
 * <p>
 * Merge hướng dẫn: paste toàn bộ method vào {@code PaymentController.java}
 * gốc, thêm dependency {@code PaymentOnlineServiceExtension} vào constructor.
 * </p>
 *
 * <pre>
 * POST  /api/payments/momo/create       → initiate MoMo payment, trả payUrl
 * POST  /api/payments/momo/ipn          → MoMo IPN server callback  (Trường whitelist IP)
 * GET   /api/payments/momo/callback     → MoMo redirect after user pays
 *
 * POST  /api/payments/vnpay/create      → initiate VNPay payment, trả redirectUrl
 * GET   /api/payments/vnpay/ipn         → VNPay IPN server callback (Trường whitelist IP)
 * GET   /api/payments/vnpay/callback    → VNPay redirect after user pays
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/api/paymentsonline")
@RequiredArgsConstructor
public class PaymentOnlineController {

	private final PaymentOnlineServiceExtension onlinePaymentService;

	// ======================================================================
	// MoMo
	// ======================================================================

	/**
	 * [FRONTEND → BACKEND] Initiate a MoMo Pay Gate payment.
	 * Returns a {@code paymentUrl} — frontend opens this URL / QR for the customer.
	 */
	@PostMapping("/momo/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<OnlinePaymentInitResponse> createMomoPayment(
			@Valid @RequestBody MomoPaymentInitRequest request) {

		OnlinePaymentInitResponse response = onlinePaymentService.initiateMomoPayment(request);

		return ApiResponse.success(response, "MoMo payment initiated successfully");
	}

	/**
	 * [MOMO → BACKEND] IPN (Instant Payment Notification) — server-to-server.
	 * MoMo calls this URL to deliver the final payment result.
	 * Trường (security) should add IP-whitelist filter for MoMo server IPs.
	 *
	 * <p>
	 * MoMo expects HTTP 200 regardless of business-logic outcome; errors are
	 * communicated via the JSON body.
	 * </p>
	 */
	@PostMapping("/momo/ipn")
	public ResponseEntity<Map<String, Object>> handleMomoIpn(
			@RequestBody MomoUtil.MomoCallbackParams params) {

		Map<String, Object> body = new HashMap<>();
		try {
			onlinePaymentService.handleMomoIpn(params);
			body.put("resultCode", 0);
			body.put("message", "IPN received successfully");
		} catch (Exception e) {
			log.error("[MoMo IPN] Processing error: {}", e.getMessage(), e);
			body.put("resultCode", 1);
			body.put("message", e.getMessage());
		}
		// Always return 200 so MoMo stops retrying
		return ResponseEntity.ok(body);
	}

	/**
	 * [BROWSER → BACKEND] Redirect callback after customer completes / cancels
	 * on MoMo. MoMo appends the same params as IPN to the redirectUrl.
	 *
	 * <p>
	 * This endpoint is NOT for payment confirmation (use IPN for that).
	 * Its only job is to redirect the customer to the appropriate frontend page.
	 * </p>
	 */
	@GetMapping("/momo/callback")
	public ResponseEntity<Void> momoRedirectCallback(
			@RequestParam(required = false, defaultValue = "") String orderId,
			@RequestParam(required = false, defaultValue = "-1") int resultCode,
			HttpServletRequest request) {

		// resultCode 0 = success
		String frontendUrl = resultCode == 0
				? "/payment/success?gateway=momo&orderId=" + orderId
				: "/payment/failed?gateway=momo&orderId=" + orderId;

		log.info("[MoMo Redirect] orderId={}, resultCode={}", orderId, resultCode);

		return ResponseEntity.status(HttpStatus.FOUND)
				.header("Location", frontendUrl)
				.build();
	}
}