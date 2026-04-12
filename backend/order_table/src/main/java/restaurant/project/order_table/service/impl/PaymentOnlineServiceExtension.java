package restaurant.project.order_table.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restaurant.project.order_table.dto.request.payment.MomoPaymentInitRequest;
import restaurant.project.order_table.dto.response.payment.OnlinePaymentInitResponse;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.*;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.PaymentRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.InvoiceService;
import restaurant.project.order_table.util.MomoUtil;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOnlineServiceExtension {

	private final PaymentRepository paymentRepository;
	private final InvoiceService invoiceService;
	private final TableRepository tableRepository;
	private final MomoUtil momoUtil;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public OnlinePaymentInitResponse initiateMomoPayment(MomoPaymentInitRequest req) {
		InvoiceEntity invoice = validateInvoiceForPayment(req.getInvoiceId());

		String orderInfo = req.getOrderInfo() != null
				? req.getOrderInfo()
				: "Thanh toan hoa don #" + req.getInvoiceId();

		MomoUtil.MomoPaymentRequest momoReq = momoUtil.buildPaymentRequest(
				req.getInvoiceId(),
				req.getAmount().longValue(),
				orderInfo);

		PaymentEntity payment = PaymentEntity.builder()
				.invoice(invoice)
				.amount(req.getAmount())
				.method(PaymentMethod.MOMO)
				.status(PaymentStatus.PENDING)
				.paidAt(LocalDateTime.now())
				.transactionCode(momoReq.getOrderId())
				.build();

		payment = paymentRepository.save(payment);

		String paymentUrl = callMomoApi(momoReq);

		return OnlinePaymentInitResponse.builder()
				.paymentId(payment.getId())
				.paymentUrl(paymentUrl)
				.gatewayOrderId(momoReq.getOrderId())
				.build();
	}

	public void handleMomoIpn(MomoUtil.MomoCallbackParams params) {
		log.info("[MoMo IPN] {}", params);

		String orderId = params.getOrderId();
		int resultCode = params.getResultCode();

		PaymentEntity payment = paymentRepository
				.findByTransactionCode(orderId)
				.orElseThrow(() -> new BadRequestException("Payment not found"));

		if (resultCode == 0) {
			payment.setStatus(PaymentStatus.SUCCESS);

			InvoiceEntity invoice = payment.getInvoice();
			invoice.setStatus(InvoiceStatus.PAID);

			TableEntity table = invoice.getTable();
			if (table != null) {
				table.setStatus(TableStatus.AVAILABLE);
				tableRepository.save(table);
			}

		} else {
			payment.setStatus(PaymentStatus.FAILED);
		}

		paymentRepository.save(payment);
	}

	private String callMomoApi(MomoUtil.MomoPaymentRequest momoReq) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<MomoUtil.MomoPaymentRequest> entity = new HttpEntity<>(momoReq, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(
					momoUtil.getApiEndpoint(),
					entity,
					String.class);

			JsonNode root = objectMapper.readTree(response.getBody());

			if (root.path("resultCode").asInt() != 0) {
				throw new BadRequestException("MoMo error: " + root.path("message").asText());
			}

			return root.path("payUrl").asText();

		} catch (Exception e) {
			throw new BadRequestException("MoMo connection error: " + e.getMessage());
		}
	}

	public String handleVnpayIpn(Map<String, String> params) {
		log.info("[VNPay IPN] {}", params);

		String txnRef = params.get("vnp_TxnRef");
		String responseCode = params.get("vnp_ResponseCode");

		PaymentEntity payment = paymentRepository
				.findByTransactionCode(txnRef)
				.orElseThrow(() -> new BadRequestException("Payment not found"));

		if ("00".equals(responseCode)) {
			payment.setStatus(PaymentStatus.SUCCESS);

			InvoiceEntity invoice = payment.getInvoice();
			invoice.setStatus(InvoiceStatus.PAID);

			TableEntity table = invoice.getTable();
			if (table != null) {
				table.setStatus(TableStatus.AVAILABLE);
				tableRepository.save(table);
			}

			paymentRepository.save(payment);

			return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
		} else {
			payment.setStatus(PaymentStatus.FAILED);
			paymentRepository.save(payment);

			return "{\"RspCode\":\"01\",\"Message\":\"Confirm Fail\"}";
		}
	}

	private InvoiceEntity validateInvoiceForPayment(Long invoiceId) {
		InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);

		if (invoice.getStatus() == InvoiceStatus.PAID) {
			throw new BadRequestException("Invoice already paid");
		}

		paymentRepository.findByInvoiceId(invoiceId).ifPresent(p -> {
			if (p.getStatus() == PaymentStatus.PENDING
					|| p.getStatus() == PaymentStatus.SUCCESS) {
				throw new BadRequestException("Payment already exists");
			}
		});

		return invoice;
	}
}