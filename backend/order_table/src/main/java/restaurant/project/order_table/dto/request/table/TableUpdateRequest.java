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
public class TableUpdateRequest {

	@NotNull(message = "Table number không được để trống")
	private Integer tableNumber;

	@NotBlank(message = "Area không được để trống")
	private String area;

	@NotNull(message = "Status không được để trống")
	private TableStatus status;

	@NotNull(message = "Active status không được để trống")
	private Boolean isActive;
}
