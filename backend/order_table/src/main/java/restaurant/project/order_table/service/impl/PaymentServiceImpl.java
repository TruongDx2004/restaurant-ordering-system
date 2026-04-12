package restaurant.project.order_table.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;
import restaurant.project.order_table.entity.enums.InvoiceStatus;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;
import restaurant.project.order_table.entity.enums.RecipientType;
import restaurant.project.order_table.entity.enums.TableStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.InvoiceItemRepository;
import restaurant.project.order_table.repository.PaymentRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.InvoiceService;
import restaurant.project.order_table.service.NotificationService;
import restaurant.project.order_table.service.PaymentService;
import restaurant.project.order_table.websocket.WebSocketService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final InvoiceService invoiceService;
	private final TableRepository tableRepository;
	private final InvoiceItemRepository invoiceItemRepository;
	private final NotificationService notificationService;
	private final WebSocketService webSocketService;

	@Override
	public PaymentEntity createPayment(PaymentEntity payment) {
		return paymentRepository.save(payment);
	}

	@Override
	public PaymentEntity getPaymentById(Long id) {
		return paymentRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Payment not found with id: " + id));
	}

	@Override
	public List<PaymentEntity> getAllPayments() {
		return paymentRepository.findAll();
	}

	@Override
	public PaymentEntity updatePayment(Long id, PaymentEntity payment) {
		PaymentEntity existing = getPaymentById(id);
		existing.setInvoice(payment.getInvoice());
		existing.setAmount(payment.getAmount());
		existing.setMethod(payment.getMethod());
		existing.setStatus(payment.getStatus());
		existing.setTransactionCode(payment.getTransactionCode());
		return paymentRepository.save(existing);
	}

	@Override
	public void deletePayment(Long id) {
		paymentRepository.delete(getPaymentById(id));
	}

	@Override
	public PaymentEntity getPaymentByInvoice(Long invoiceId) {
		return paymentRepository.findByInvoiceId(invoiceId)
				.orElseThrow(() -> new BadRequestException("Payment not found for invoice: " + invoiceId));
	}

	@Override
	public PaymentEntity getPaymentByTransactionCode(String transactionCode) {
		return paymentRepository.findByTransactionCode(transactionCode)
				.orElseThrow(() -> new BadRequestException("Payment not found: " + transactionCode));
	}

	@Override
	public List<PaymentEntity> getPaymentsByStatus(PaymentStatus status) {
		return paymentRepository.findByStatus(status);
	}

	@Override
	public List<PaymentEntity> getPaymentsByMethod(PaymentMethod method) {
		return paymentRepository.findByMethod(method);
	}

	@Override
	public PaymentEntity updatePaymentStatus(Long id, PaymentStatus status) {
		PaymentEntity payment = getPaymentById(id);
		payment.setStatus(status);
		if (status == PaymentStatus.SUCCESS) {
			payment.setPaidAt(LocalDateTime.now());
			invoiceService.updateInvoiceStatus(payment.getInvoice().getId(), InvoiceStatus.PAID);
		}
		return paymentRepository.save(payment);
	}

	@Override
	@Transactional
	public PaymentEntity processPayment(Long invoiceId, PaymentMethod method, BigDecimal amount) {
		InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);

		if (invoice.getStatus() == InvoiceStatus.PAID) {
			throw new BadRequestException("Invoice is already paid");
		}

		List<InvoiceItemEntity> items = invoiceItemRepository.findByInvoiceId(invoiceId);
		boolean hasUnserved = items.stream().anyMatch(
				item -> item.getStatus() != InvoiceItemStatus.SERVED
						&& item.getStatus() != InvoiceItemStatus.CANCELLED);
		if (hasUnserved) {
			throw new BadRequestException("Không thể thanh toán khi còn món chưa được phục vụ");
		}

		paymentRepository.findByInvoiceId(invoiceId).ifPresent(p -> {
			throw new BadRequestException("Payment already exists for this invoice");
		});

		PaymentEntity payment = new PaymentEntity();
		payment.setInvoice(invoice);
		payment.setMethod(method);
		payment.setAmount(amount);
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaidAt(LocalDateTime.now());
		payment.setTransactionCode(generateTransactionCode());
		payment = paymentRepository.save(payment);

		invoiceService.updateInvoiceStatus(invoice.getId(), InvoiceStatus.PAID);

		TableEntity table = invoice.getTable();
		if (table != null && table.getStatus() == TableStatus.OCCUPIED) {
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}

		return payment;
	}

	@Override
	@Transactional
	public PaymentEntity confirmPayment(Long id, String transactionCode) {
		PaymentEntity payment = getPaymentById(id);
		if (payment.getStatus() == PaymentStatus.SUCCESS) {
			throw new BadRequestException("Payment already completed");
		}
		payment.setTransactionCode(transactionCode);
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setPaidAt(LocalDateTime.now());

		InvoiceEntity invoice = payment.getInvoice();
		invoiceService.updateInvoiceStatus(invoice.getId(), InvoiceStatus.PAID);

		TableEntity table = invoice.getTable();
		if (table != null && table.getStatus() == TableStatus.OCCUPIED) {
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}
		return paymentRepository.save(payment);
	}

	@Override
	public PaymentEntity cancelPayment(Long id) {
		PaymentEntity payment = getPaymentById(id);
		if (payment.getStatus() == PaymentStatus.SUCCESS) {
			throw new BadRequestException("Cannot cancel completed payment");
		}
		payment.setStatus(PaymentStatus.FAILED);
		return paymentRepository.save(payment);
	}

	@Override
    @Transactional
    public void requestCashPayment(Long invoiceId, Long tableId, BigDecimal amount) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);

        List<InvoiceItemEntity> items = invoiceItemRepository.findByInvoiceId(invoiceId);
        boolean hasUnserved = items.stream().anyMatch(
                item -> item.getStatus() != InvoiceItemStatus.SERVED
                        && item.getStatus() != InvoiceItemStatus.CANCELLED);
        if (hasUnserved) {
            throw new BadRequestException("Không thể yêu cầu thanh toán khi còn món chưa được phục vụ");
        }

        paymentRepository.findByInvoiceId(invoiceId).ifPresent(p -> {
            if (p.getStatus() == PaymentStatus.SUCCESS) {
                throw new BadRequestException("Hóa đơn đã được thanh toán thành công trước đó");
            }
        });
)
        String amountFormatted = String.format("%,.0f", amount.doubleValue());
        notificationService.createAndSend(
                RecipientType.USER, 0L,
                "Yêu cầu thanh toán tiền mặt",
                "Bàn " + tableId + " yêu cầu thanh toán " + amountFormatted + " VND.",
                "CASH_PAYMENT_REQUEST",
                Map.of("invoiceId", invoiceId, "tableId", tableId,
                        "amount", amount, "action", "CONFIRM_PAYMENT"));
    }

	@Override
	@Transactional
	public PaymentEntity confirmPaymentByInvoice(Long invoiceId, String transactionCode) {
		LocalDateTime now = LocalDateTime.now();
		String txCode = (transactionCode != null && !transactionCode.isBlank())
				? transactionCode
				: "CASH-" + System.currentTimeMillis();

		PaymentEntity payment = paymentRepository.findByInvoiceId(invoiceId).orElse(null);

		if (payment == null) {
			InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
			payment = new PaymentEntity();
			payment.setInvoice(invoice);
			payment.setAmount(invoice.getTotalAmount());
			payment.setMethod(PaymentMethod.CASH);
		}

		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setTransactionCode(txCode);
		payment.setPaidAt(now);
		payment = paymentRepository.save(payment);

		invoiceService.updateInvoiceStatus(invoiceId, InvoiceStatus.PAID);

		TableEntity table = payment.getInvoice().getTable();
		if (table != null) {
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}

		notificationService.createAndSend(
				RecipientType.USER, 0L,
				"Xác nhận thanh toán",
				"Hóa đơn #" + invoiceId + " (Bàn " + (table != null ? table.getId() : "?")
						+ ") đã được xác nhận thanh toán thành công.",
				"PAYMENT_SUCCESS",
				Map.of("invoiceId", invoiceId,
						"tableId", table != null ? table.getId() : 0L));

		webSocketService.sendPaymentNotification(invoiceId,
				table != null ? table.getId() : 0L, "PAID");
		webSocketService.sendTableStatusUpdate(
				table != null ? table.getId() : 0L, "AVAILABLE", null);

		return payment;
	}

	private String generateTransactionCode() {
		return "TXN-" + System.currentTimeMillis() + "-"
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
}
