package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Giá tại thời điểm order */
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    /** Tổng tiền = quantity * unitPrice */
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}
