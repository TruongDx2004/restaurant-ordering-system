package restaurant.project.order_table.dto.response.dish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.dto.response.category.CategoryResponse;
import restaurant.project.order_table.entity.enums.DishStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishResponse {

    private Long id;
    private String name;
    private Integer price;
    private DishStatus status;
    private String image;
    private CategoryResponse category;
}
