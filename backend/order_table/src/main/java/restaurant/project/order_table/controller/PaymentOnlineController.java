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

@Slf4j
@RestController
@RequestMapping("/api/paymentsonline")
@RequiredArgsConstructor
public class PaymentOnlineController {

	private final PaymentOnlineServiceExtension onlinePaymentService;

	@PostMapping("/momo/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<OnlinePaymentInitResponse> createMomoPayment(
			@Valid @RequestBody MomoPaymentInitRequest request) {

		OnlinePaymentInitResponse response = onlinePaymentService.initiateMomoPayment(request);

		return ApiResponse.success(response, "Tạo thanh toán MoMo thành công, khách hàng sẽ được chuyển hướng đến trang thanh toán của MoMo");
	}

	@PostMapping("/momo/ipn")
	public ResponseEntity<Map<String, Object>> handleMomoIpn(
			@RequestBody MomoUtil.MomoCallbackParams params) {

		Map<String, Object> body = new HashMap<>();
		try {
			onlinePaymentService.handleMomoIpn(params);
			body.put("resultCode", 0);
			body.put("message", "IPN processed successfully");
		} catch (Exception e) {
			log.error("[MoMo IPN] Processing error: {}", e.getMessage(), e);
			body.put("resultCode", 1);
			body.put("message", e.getMessage());
		}
		// Always return 200 so MoMo stops retrying
		return ResponseEntity.ok(body);
	}

	@GetMapping("/momo/callback")
	public ResponseEntity<Void> momoRedirectCallback(
			@RequestParam(required = false, defaultValue = "") String orderId,
			@RequestParam(required = false, defaultValue = "-1") int resultCode,
			HttpServletRequest request) {

		String frontendUrl = resultCode == 0
				? "/payment/success?gateway=momo&orderId=" + orderId
				: "/payment/failed?gateway=momo&orderId=" + orderId;

		log.info("[MoMo Redirect] orderId={}, resultCode={}", orderId, resultCode);

		return ResponseEntity.status(HttpStatus.FOUND)
				.header("Location", frontendUrl)
				.build();
	}
}