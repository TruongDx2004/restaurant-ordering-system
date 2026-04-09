package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@SQLDelete(sql = "UPDATE payments SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    /** Phương thức (CASH / VNPAY / MOMO) */
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethod method;

    /** Trạng thái (PENDING / SUCCESS / FAILED) */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    /** Thời điểm thanh toán – nullable (chưa thanh toán = null) */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /** Mã giao dịch (VNPay, MoMo…) */
    @Column(name = "transaction_code", length = 255)
    private String transactionCode;

    /** Thời điểm tạo */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm cập nhật */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Soft delete */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
