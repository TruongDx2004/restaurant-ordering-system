package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Thuộc hóa đơn nào */
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

    /** Món ăn */
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private DishEntity dish;

    /** Số lượng */
    @Column(nullable = false)
    private Integer quantity;

    /** Giá tại thời điểm order */
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    /** Tổng tiền = quantity * unitPrice */
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    /** 🔥 Trạng thái món */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceItemStatus status;

    /** 📝 Ghi chú từ khách */
    @Column(columnDefinition = "TEXT")
    private String note;

    /** 🕒 Thời gian tạo */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 🕒 Thời gian cập nhật */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
