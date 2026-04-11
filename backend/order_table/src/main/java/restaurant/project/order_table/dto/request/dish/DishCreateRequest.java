package restaurant.project.order_table.dto.request.dish;

import org.springframework.web.multipart.MultipartFile;

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

    @NotBlank(message = "Dish name không được để trống")
    @Size(max = 255, message = "Dish name không được quá 255 kí tự")
    private String name;

    @NotNull(message = "Price không được để trống")
    @Min(value = 0, message = "Gía phải lớn hơn hoặc bằng 0")
    private Integer price;

    @NotNull(message = "Status không được để trống")
    private DishStatus status;

    @Size(max = 255, message = "Image URL không được quá 255 kí tự")
    private MultipartFile image;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;
}
