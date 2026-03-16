package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;

import restaurant.project.order_table.entity.enums.TableStatus;

@Entity
@Table(
    name = "tables",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "table_number")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Số bàn */
    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    /** Khu vực */
    @Column(name = "area", nullable = false)
    private String area;

    /** Trạng thái bàn */
    @Enumerated(EnumType.STRING)    
    @Column(name = "status", nullable = false)
    private TableStatus status;

    /** Bàn có hoạt động không */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
