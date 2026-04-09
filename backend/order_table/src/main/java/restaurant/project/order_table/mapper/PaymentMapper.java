package restaurant.project.order_table.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.payment.PaymentCreateRequest;
import restaurant.project.order_table.dto.request.payment.PaymentProcessRequest;
import restaurant.project.order_table.dto.response.payment.PaymentResponse;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.service.InvoiceService;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final InvoiceService invoiceService;

    public PaymentEntity toEntity(PaymentCreateRequest request) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(request.getInvoiceId());
        
        return PaymentEntity.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(request.getStatus())
                .transactionCode(request.getTransactionCode())
                .build();
    }

    public PaymentEntity toEntity(PaymentProcessRequest request) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(request.getInvoiceId());
        
        return PaymentEntity.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .method(request.getMethod())
                .build();
    }

    public PaymentResponse toResponse(PaymentEntity entity) {
        return PaymentResponse.builder()
                .id(entity.getId())
                .invoiceId(entity.getInvoice().getId())
                .amount(entity.getAmount())
                .method(entity.getMethod())
                .status(entity.getStatus())
                .paidAt(entity.getPaidAt())
                .transactionCode(entity.getTransactionCode())
                .build();
    }

    public List<PaymentResponse> toResponseList(List<PaymentEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
