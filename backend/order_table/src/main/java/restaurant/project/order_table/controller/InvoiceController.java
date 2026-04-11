package restaurant.project.order_table.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.invoice.InvoiceCreateRequest;
import restaurant.project.order_table.dto.request.invoice.InvoiceUpdateRequest;
import restaurant.project.order_table.dto.request.invoice.InvoiceWithItemsRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.invoice.InvoiceResponse;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.enums.InvoiceStatus;
import restaurant.project.order_table.mapper.InvoiceMapper;
import restaurant.project.order_table.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) {
        InvoiceEntity entity = invoiceMapper.toEntity(request);
        InvoiceEntity created = invoiceService.createInvoice(entity);
        return ApiResponse.success(invoiceMapper.toResponse(created), "Hóa đơn đã được tạo thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        InvoiceEntity invoice = invoiceService.getInvoiceById(id);
        return ApiResponse.success(invoiceMapper.toResponse(invoice), "Hóa đơn đã được lấy thành công");
    }

    @GetMapping
    public ApiResponse<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoices = invoiceMapper.toResponseList(invoiceService.getAllInvoices());
        return ApiResponse.success(invoices, "Hóa đơn đã được lấy thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<InvoiceResponse> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceUpdateRequest request) {
        InvoiceEntity entity = invoiceMapper.toEntity(request);
        InvoiceEntity updated = invoiceService.updateInvoice(id, entity);
        return ApiResponse.success(invoiceMapper.toResponse(updated), "Hóa đơn đã được cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ApiResponse.success(null, "Hóa đơn đã được xóa thành công");
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<InvoiceResponse>> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        List<InvoiceResponse> invoices = invoiceMapper.toResponseList(invoiceService.getInvoicesByStatus(status));
        return ApiResponse.success(invoices, "Hóa đơn đã được lấy thành công");
    }

    @GetMapping("/table/{tableId}")
    public ApiResponse<List<InvoiceResponse>> getInvoicesByTable(@PathVariable Long tableId) {
        List<InvoiceResponse> invoices = invoiceMapper.toResponseList(invoiceService.getInvoicesByTable(tableId));
        return ApiResponse.success(invoices, "Hóa đơn đã được lấy thành công");
    }

    @GetMapping("/date-range")
    public ApiResponse<List<InvoiceResponse>> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<InvoiceResponse> invoices = invoiceMapper.toResponseList(invoiceService.getInvoicesByDateRange(startDate, endDate));
        return ApiResponse.success(invoices, "Hóa đơn đã được lấy thành công");
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<InvoiceResponse> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status) {
        InvoiceEntity updated = invoiceService.updateInvoiceStatus(id, status);
        return ApiResponse.success(invoiceMapper.toResponse(updated), "Hóa đơn đã được cập nhật trạng thái thành công");
    }

    @GetMapping("/table/{tableId}/active")
    public ApiResponse<InvoiceResponse> getActiveInvoiceByTable(@PathVariable Long tableId) {
        InvoiceEntity invoice = invoiceService.getActiveInvoiceByTable(tableId);
        return ApiResponse.success(invoice != null ? invoiceMapper.toResponse(invoice) : null, "Hóa đơn hoạt động đã được lấy thành công");
    }

    @GetMapping("/table-number/{tableNumber}/active")
    public ApiResponse<InvoiceResponse> getActiveInvoiceByTableNumber(@PathVariable Integer tableNumber) {
        InvoiceEntity invoice = invoiceService.getActiveInvoiceByTableNumber(tableNumber);
        return ApiResponse.success(invoice != null ? invoiceMapper.toResponse(invoice) : null, "Hóa đơn hoạt động đã được lấy thành công");
    }

    @GetMapping("/{id}/calculate-total")
    public ApiResponse<BigDecimal> calculateInvoiceTotal(@PathVariable Long id) {
        BigDecimal total = invoiceService.calculateInvoiceTotal(id);
        return ApiResponse.success(total, "Tổng giá trị hóa đơn đã được tính toán thành công");
    }

    @PostMapping("/create-with-items")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceResponse> createInvoiceWithItems(@Valid @RequestBody InvoiceWithItemsRequest request) {
        List<InvoiceService.ItemData> items = request.getItems().stream()
                .map(item -> new InvoiceService.ItemData(item.getDishId(), item.getQuantity(), item.getStatus(),item.getNotes()))
                .toList();
        InvoiceEntity created = invoiceService.createInvoiceWithItems(request.getTableId(), items);
        return ApiResponse.success(invoiceMapper.toResponse(created), "Hóa đơn đã được tạo thành công");
    }
}
