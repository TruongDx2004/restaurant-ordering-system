package restaurant.project.order_table.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.payment.PaymentCreateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.payment.PaymentResponse;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;
import restaurant.project.order_table.mapper.PaymentMapper;
import restaurant.project.order_table.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    /**
     * Create a new payment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentEntity entity = paymentMapper.toEntity(request);
        PaymentEntity created = paymentService.createPayment(entity);
        return ApiResponse.success(paymentMapper.toResponse(created), "Payment created successfully");
    }

    /**
     * Get payment by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentEntity payment = paymentService.getPaymentById(id);
        return ApiResponse.success(paymentMapper.toResponse(payment), "Payment retrieved successfully");
    }

    /**
     * Get all payments
     */
    @GetMapping
    public ApiResponse<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentMapper.toResponseList(paymentService.getAllPayments());
        return ApiResponse.success(payments, "Payments retrieved successfully");
    }

    /**
     * Update payment
     */
    @PutMapping("/{id}")
    public ApiResponse<PaymentResponse> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentCreateRequest request) {
        PaymentEntity entity = paymentMapper.toEntity(request);
        PaymentEntity updated = paymentService.updatePayment(id, entity);
        return ApiResponse.success(paymentMapper.toResponse(updated), "Payment updated successfully");
    }

    /**
     * Delete payment
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ApiResponse.success(null, "Payment deleted successfully");
    }

    /**
     * Get payment by invoice
     */
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<PaymentResponse> getPaymentByInvoice(@PathVariable Long invoiceId) {
        PaymentEntity payment = paymentService.getPaymentByInvoice(invoiceId);
        return ApiResponse.success(paymentMapper.toResponse(payment), "Payment retrieved successfully");
    }

    /**
     * Get payment by transaction code
     */
    @GetMapping("/transaction/{transactionCode}")
    public ApiResponse<PaymentResponse> getPaymentByTransactionCode(@PathVariable String transactionCode) {
        PaymentEntity payment = paymentService.getPaymentByTransactionCode(transactionCode);
        return ApiResponse.success(paymentMapper.toResponse(payment), "Payment retrieved successfully");
    }

    /**
     * Get payments by status
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> payments = paymentMapper.toResponseList(paymentService.getPaymentsByStatus(status));
        return ApiResponse.success(payments, "Payments retrieved successfully");
    }

    /**
     * Get payments by method
     */
    @GetMapping("/method/{method}")
    public ApiResponse<List<PaymentResponse>> getPaymentsByMethod(@PathVariable PaymentMethod method) {
        List<PaymentResponse> payments = paymentMapper.toResponseList(paymentService.getPaymentsByMethod(method));
        return ApiResponse.success(payments, "Payments retrieved successfully");
    }

    /**
     * Update payment status
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {
        PaymentEntity updated = paymentService.updatePaymentStatus(id, status);
        return ApiResponse.success(paymentMapper.toResponse(updated), "Payment status updated successfully");
    }

    /**
     * Process payment for invoice
     */
    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> processPayment(
            @RequestParam Long invoiceId,
            @RequestParam PaymentMethod method,
            @RequestParam BigDecimal amount) {
        PaymentEntity payment = paymentService.processPayment(invoiceId, method, amount);
        return ApiResponse.success(paymentMapper.toResponse(payment), "Payment processed successfully");
    }

    /**
     * Confirm payment
     */
    @PatchMapping("/{id}/confirm")
    public ApiResponse<PaymentResponse> confirmPayment(
            @PathVariable Long id,
            @RequestParam String transactionCode) {
        PaymentEntity updated = paymentService.confirmPayment(id, transactionCode);
        return ApiResponse.success(paymentMapper.toResponse(updated), "Payment confirmed successfully");
    }

    /**
     * Cancel payment
     */
    @PatchMapping("/{id}/cancel")
    public ApiResponse<PaymentResponse> cancelPayment(@PathVariable Long id) {
        PaymentEntity updated = paymentService.cancelPayment(id);
        return ApiResponse.success(paymentMapper.toResponse(updated), "Payment cancelled successfully");
    }
}
