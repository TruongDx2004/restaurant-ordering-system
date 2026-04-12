package restaurant.project.order_table.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.payment.CashPaymentRequest;
import restaurant.project.order_table.dto.request.payment.ConfirmPaymentRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.payment.PaymentResponse;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.mapper.PaymentMapper;
import restaurant.project.order_table.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping
    public ApiResponse<List<PaymentResponse>> getAllPayments() {
        return ApiResponse.success(
                paymentMapper.toResponseList(paymentService.getAllPayments()),
                "Các thanh toán được lấy thành công");
    }

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> processPayment(
            @RequestParam Long invoiceId,
            @RequestParam PaymentMethod method,
            @RequestParam BigDecimal amount) {
        PaymentEntity payment = paymentService.processPayment(invoiceId, method, amount);
        return ApiResponse.success(paymentMapper.toResponse(payment), "Thanh toán được xử lý thành công");
    }

    @PostMapping("/request-cash")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> requestCashPayment(@Valid @RequestBody CashPaymentRequest request) {
        paymentService.requestCashPayment(request.getInvoiceId(), request.getTableId(), request.getAmount());
        return ApiResponse.success(null, "Yêu cầu thanh toán bằng tiền mặt đã được gửi đến tất cả nhân viên");
    }

    @PatchMapping("/confirm-by-invoice")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ApiResponse<PaymentResponse> confirmPaymentByInvoice(@Valid @RequestBody ConfirmPaymentRequest request) {
        PaymentEntity payment = paymentService.confirmPaymentByInvoice(
                request.getInvoiceId(), request.getTransactionCode());
        return ApiResponse.success(paymentMapper.toResponse(payment), "Thanh toán đã được xác nhận");
    }
}
