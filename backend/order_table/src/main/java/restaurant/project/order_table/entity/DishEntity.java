package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;

import restaurant.project.order_table.entity.enums.DishStatus;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Tên món ăn */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /** Giá món ăn */
    @Column(name = "price", nullable = false)
    private Integer price;

    /** Trạng thái món ăn */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DishStatus status;

    /** Hình ảnh món ăn */
    @Column(name = "image", length = 255)
    private String image;

    /**
     * Many Dishes -> One Category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
