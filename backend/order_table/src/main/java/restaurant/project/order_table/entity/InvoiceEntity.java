package restaurant.project.order_table.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;
import restaurant.project.order_table.entity.enums.InvoiceStatus;

@Entity
@Table(name = "invoices")
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

    /** Thời điểm thanh toán */
    private LocalDateTime paidAt;

    /** Danh sách món */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItemEntity> items;
}
