package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import restaurant.project.order_table.entity.enums.InvoiceStatus;

@Entity
@Table(name = "invoices")
@SQLDelete(sql = "UPDATE invoices SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Bàn nào */
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableEntity table;

    /** Tổng tiền */
    @Column(nullable = false)
    private BigDecimal totalAmount;

    /** Trạng thái hóa đơn */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    /** Thời điểm tạo */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm cập nhật */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Thời điểm thanh toán */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /** Soft delete */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /** Danh sách món */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItemEntity> items;
}
