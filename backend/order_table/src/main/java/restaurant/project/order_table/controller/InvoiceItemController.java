package restaurant.project.order_table.controller;

import java.util.List;

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
import restaurant.project.order_table.dto.request.invoiceitem.InvoiceItemCreateRequest;
import restaurant.project.order_table.dto.request.invoiceitem.InvoiceItemUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.invoiceitem.InvoiceItemResponse;
import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;
import restaurant.project.order_table.mapper.InvoiceItemMapper;
import restaurant.project.order_table.service.InvoiceItemService;

@RestController
@RequestMapping("/api/invoice-items")
@RequiredArgsConstructor
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;
    private final InvoiceItemMapper invoiceItemMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceItemResponse> createInvoiceItem(@Valid @RequestBody InvoiceItemCreateRequest request) {
        InvoiceItemEntity entity = invoiceItemMapper.toEntity(request);
        InvoiceItemEntity created = invoiceItemService.createInvoiceItem(entity);
        return ApiResponse.success(invoiceItemMapper.toResponse(created), "Mặt hàng hóa đơn đã được tạo thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<InvoiceItemResponse> getInvoiceItemById(@PathVariable Long id) {
        InvoiceItemEntity invoiceItem = invoiceItemService.getInvoiceItemById(id);
        return ApiResponse.success(invoiceItemMapper.toResponse(invoiceItem), "Mặt hàng hóa đơn đã được lấy thành công");
    }

    @GetMapping
    public ApiResponse<List<InvoiceItemResponse>> getAllInvoiceItems() {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getAllInvoiceItems());
        return ApiResponse.success(invoiceItems, "Mặt hàng hóa đơn đã được lấy thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<InvoiceItemResponse> updateInvoiceItem(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceItemUpdateRequest request) {
        InvoiceItemEntity entity = invoiceItemMapper.toEntity(request);
        InvoiceItemEntity updated = invoiceItemService.updateInvoiceItem(id, entity);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Mặt hàng hóa đơn đã được cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInvoiceItem(@PathVariable Long id) {
        invoiceItemService.deleteInvoiceItem(id);
        return ApiResponse.success(null, "Mặt hàng hóa đơn đã được xóa thành công");
    }

    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<InvoiceItemResponse>> getInvoiceItemsByInvoice(@PathVariable Long invoiceId) {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getInvoiceItemsByInvoice(invoiceId));
        return ApiResponse.success(invoiceItems, "Mặt hàng hóa đơn đã được lấy thành công");
    }

    @GetMapping("/dish/{dishId}")
    public ApiResponse<List<InvoiceItemResponse>> getInvoiceItemsByDish(@PathVariable Long dishId) {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getInvoiceItemsByDish(dishId));
        return ApiResponse.success(invoiceItems, "Mặt hàng hóa đơn đã được lấy thành công");
    }

    @PatchMapping("/{id}/quantity")
    public ApiResponse<InvoiceItemResponse> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        InvoiceItemEntity updated = invoiceItemService.updateQuantity(id, quantity);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Mặt hàng hóa đơn đã được cập nhật số lượng thành công");
    }

    @PostMapping("/add-to-invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceItemResponse> addItemToInvoice(
            @RequestParam Long invoiceId,
            @RequestParam Long dishId,
            @RequestParam Integer quantity) {
        InvoiceItemEntity created = invoiceItemService.addItemToInvoice(invoiceId, dishId, quantity);
        return ApiResponse.success(invoiceItemMapper.toResponse(created), "Mặt hàng đã được thêm vào hóa đơn thành công");
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<InvoiceItemResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam InvoiceItemStatus status) {
        InvoiceItemEntity updated = invoiceItemService.updateStatus(id, status);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Trạng thái mặt hàng hóa đơn đã được cập nhật thành công");
    }
}
