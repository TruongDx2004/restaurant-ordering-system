package restaurant.project.order_table.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.UUID;

@Slf4j
@Getter
@Component
public class MomoUtil {

	@Value("${momo.partner-code}")
	private String partnerCode;

	@Value("${momo.access-key}")
	private String accessKey;

	@Value("${momo.secret-key}")
	private String secretKey;

	@Value("${momo.redirect-url}")
	private String redirectUrl; // returnUrl

	@Value("${momo.ipn-url}")
	private String ipnUrl; // notifyUrl

	@Value("${momo.api-endpoint}")
	private String apiEndpoint;

	// =========================================================
	// CREATE PAYMENT REQUEST
	// =========================================================
	public MomoPaymentRequest buildPaymentRequest(Long invoiceId, long amount, String orderInfo) {

		String requestId = UUID.randomUUID().toString();
		String orderId = "ORDER-" + invoiceId + "-" + System.currentTimeMillis();

		String extraData = "";
		String requestType = "captureWallet";

		// ✅ SIGNATURE CHUẨN (KHÔNG có partnerName, storeId)
		String rawSignature = "accessKey=" + accessKey
				+ "&amount=" + amount
				+ "&extraData=" + extraData
				+ "&ipnUrl=" + ipnUrl
				+ "&orderId=" + orderId
				+ "&orderInfo=" + orderInfo
				+ "&partnerCode=" + partnerCode
				+ "&redirectUrl=" + redirectUrl
				+ "&requestId=" + requestId
				+ "&requestType=" + requestType;

		String signature = hmacSHA256(secretKey, rawSignature);

		log.info("[MoMo DEBUG] Raw Signature: {}", rawSignature);
		log.info("[MoMo DEBUG] Generated Signature: {}", signature);

		return MomoPaymentRequest.builder()
				.partnerCode(partnerCode) //
				.partnerName("BCB BADMINTON") //
				.storeId("RBssxkdMJqFK44MM") //
				// .accessKey(accessKey)
				.requestId(requestId) //
				.amount(amount) //
				.orderId(orderId) //
				.orderInfo(orderInfo) //
				.redirectUrl(redirectUrl) //
				.ipnUrl(ipnUrl) //
				.extraData(extraData) //
				.requestType(requestType) //
				.signature(signature) //
				.lang("vi") //
				.build();
	}

	// =========================================================
	// HMAC SHA256
	// =========================================================
	private String hmacSHA256(String key, String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(raw);
		} catch (Exception e) {
			throw new RuntimeException("Failed to compute HMAC-SHA256", e);
		}
	}

	// =========================================================
	// REQUEST DTO
	// =========================================================
	@lombok.Data
	@lombok.Builder
	@lombok.NoArgsConstructor
	@lombok.AllArgsConstructor
	public static class MomoPaymentRequest {
		private String partnerCode;
		private String partnerName; // ✅ chỉ JSON
		private String storeId; // ✅ chỉ JSON
		private String accessKey;
		private String requestId;
		private long amount;
		private String orderId;
		private String orderInfo;
		private String redirectUrl;
		private String ipnUrl;
		private String extraData;
		private String requestType;
		private String signature;
		private String lang;
	}

	// =========================================================
	// IPN CALLBACK DTO
	// =========================================================
	@lombok.Data
	@lombok.NoArgsConstructor
	@lombok.AllArgsConstructor
	public static class MomoCallbackParams {
		private String orderId;
		private int resultCode;
		private String message;
		private String transId;
		private String amount;
	}
}