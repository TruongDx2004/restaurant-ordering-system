package restaurant.project.order_table.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.InvoiceStatus;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;
import restaurant.project.order_table.entity.enums.TableStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.PaymentRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.InvoiceService;
import restaurant.project.order_table.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final TableRepository tableRepository;

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
        PaymentEntity existingPayment = getPaymentById(id);

        existingPayment.setInvoice(payment.getInvoice());
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setMethod(payment.getMethod());
        existingPayment.setStatus(payment.getStatus());
        existingPayment.setTransactionCode(payment.getTransactionCode());

        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long id) {
        PaymentEntity payment = getPaymentById(id);
        paymentRepository.delete(payment);
    }

    @Override
    public PaymentEntity getPaymentByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new BadRequestException("Payment not found for invoice id: " + invoiceId));
    }

    @Override
    public PaymentEntity getPaymentByTransactionCode(String transactionCode) {
        return paymentRepository.findByTransactionCode(transactionCode)
                .orElseThrow(() -> new BadRequestException("Payment not found with transaction code: " + transactionCode));
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
            
            // Update invoice status to PAID
            InvoiceEntity invoice = payment.getInvoice();
            invoiceService.updateInvoiceStatus(invoice.getId(), InvoiceStatus.PAID);
        }
        
        return paymentRepository.save(payment);
    }

    @Override
    public PaymentEntity processPayment(Long invoiceId, PaymentMethod method, BigDecimal amount) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
        
        // Check if payment already exists for this invoice
        paymentRepository.findByInvoiceId(invoiceId)
                .ifPresent(p -> {
                    throw new BadRequestException("Payment already exists for this invoice");
                });

        // Validate invoice status
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice is already paid");
        }

        // Create payment
        PaymentEntity payment = new PaymentEntity();
        payment.setInvoice(invoice);
        payment.setMethod(method);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.SUCCESS); // Set to SUCCESS immediately for simple flow
        payment.setPaidAt(LocalDateTime.now());
        
        // Generate transaction code for tracking
        payment.setTransactionCode(generateTransactionCode());
        
        // Save payment first
        payment = paymentRepository.save(payment);
        
        // Update invoice status to PAID
        invoiceService.updateInvoiceStatus(invoice.getId(), InvoiceStatus.PAID);
        
        // Update table status to AVAILABLE (trả bàn)
        TableEntity table = invoice.getTable();
        if (table != null && table.getStatus() == TableStatus.OCCUPIED) {
            table.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(table);
        }

        return payment;
    }

    @Override
    public PaymentEntity confirmPayment(Long id, String transactionCode) {
        PaymentEntity payment = getPaymentById(id);
        
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already completed");
        }
        
        payment.setTransactionCode(transactionCode);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        
        // Update invoice status to PAID
        InvoiceEntity invoice = payment.getInvoice();
        invoiceService.updateInvoiceStatus(invoice.getId(), InvoiceStatus.PAID);
        
        // Update table status to AVAILABLE (trả bàn)
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
        
        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment);
    }

    /**
     * Generate unique transaction code
     *
     * @return transaction code
     */
    private String generateTransactionCode() {
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
