package restaurant.project.order_table.dto.response.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.dto.response.invoiceitem.InvoiceItemResponse;
import restaurant.project.order_table.dto.response.table.TableResponse;
import restaurant.project.order_table.entity.enums.InvoiceStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private Long id;
    private TableResponse table;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime paidAt;
    private List<InvoiceItemResponse> items;
}
