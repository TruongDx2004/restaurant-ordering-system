package restaurant.project.order_table.dto.response.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.TableStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableResponse {

    private Long id;
    private Integer tableNumber;
    private String area;
    private TableStatus status;
    private Boolean isActive;
}
