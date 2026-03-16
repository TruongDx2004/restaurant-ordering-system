package restaurant.project.order_table.dto.response.invoiceitem;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.dto.response.dish.DishResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItemResponse {

    private Long id;
    private DishResponse dish;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
