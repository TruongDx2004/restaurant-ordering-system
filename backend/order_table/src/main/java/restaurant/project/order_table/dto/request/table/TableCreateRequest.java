package restaurant.project.order_table.dto.request.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.TableStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableCreateRequest {

    @NotNull(message = "Table number is required")
    private Integer tableNumber;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "Status is required")
    private TableStatus status;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
