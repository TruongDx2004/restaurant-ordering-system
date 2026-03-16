package restaurant.project.order_table.dto.request.invoice;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating invoice with items
 * Used when customer places an order from cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceWithItemsRequest {

    @NotNull(message = "Table ID is required")
    private Long tableId;

    @NotEmpty(message = "Items are required")
    @Valid
    private List<ItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemRequest {
        
        @NotNull(message = "Dish ID is required")
        private Long dishId;
        
        @NotNull(message = "Quantity is required")
        private Integer quantity;
        
        private String notes;
    }
}
