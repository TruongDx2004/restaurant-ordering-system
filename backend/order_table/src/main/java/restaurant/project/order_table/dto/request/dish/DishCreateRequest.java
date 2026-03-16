package restaurant.project.order_table.dto.request.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.DishStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishCreateRequest {

    @NotBlank(message = "Dish name is required")
    @Size(max = 255, message = "Dish name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Integer price;

    @NotNull(message = "Status is required")
    private DishStatus status;

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String image;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
