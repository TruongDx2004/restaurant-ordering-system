package restaurant.project.order_table.dto.request.invoice;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceWithItemsRequest {

    @NotNull(message = "Table ID không được để trống")
    private Long tableId;

    @NotEmpty(message = "Items are required")
    @Valid
    private List<ItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemRequest {
        
        @NotNull(message = "Dish ID không được để trống")
        private Long dishId;
        
        @NotNull(message = "Quantity không được để trống")
        private Integer quantity;

        @Builder.Default
        @NotNull(message = "Status không được để trống")
        private InvoiceItemStatus status = InvoiceItemStatus.WAITING; // WAITING, PREPARING, SERVED, CANCELLED
        
        private String notes;
    }
}
