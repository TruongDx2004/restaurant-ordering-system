package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Hóa đơn được thanh toán */
    @OneToOne
    @JoinColumn(name = "invoice_id", nullable = false, unique = true)
    private InvoiceEntity invoice;

    /** Số tiền thanh toán */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /** Phương thức */
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    /** Trạng thái */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    /** Thời điểm thanh toán */
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    /** Mã giao dịch (VNPay, Momo...) */
    @Column(name = "transaction_code", length = 255)
    private String transactionCode;
}
