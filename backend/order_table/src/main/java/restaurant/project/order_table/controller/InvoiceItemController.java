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

    /**
     * Create a new invoice item
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceItemResponse> createInvoiceItem(@Valid @RequestBody InvoiceItemCreateRequest request) {
        InvoiceItemEntity entity = invoiceItemMapper.toEntity(request);
        InvoiceItemEntity created = invoiceItemService.createInvoiceItem(entity);
        return ApiResponse.success(invoiceItemMapper.toResponse(created), "Invoice item created successfully");
    }

    /**
     * Get invoice item by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<InvoiceItemResponse> getInvoiceItemById(@PathVariable Long id) {
        InvoiceItemEntity invoiceItem = invoiceItemService.getInvoiceItemById(id);
        return ApiResponse.success(invoiceItemMapper.toResponse(invoiceItem), "Invoice item retrieved successfully");
    }

    /**
     * Get all invoice items
     */
    @GetMapping
    public ApiResponse<List<InvoiceItemResponse>> getAllInvoiceItems() {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getAllInvoiceItems());
        return ApiResponse.success(invoiceItems, "Invoice items retrieved successfully");
    }

    /**
     * Update invoice item
     */
    @PutMapping("/{id}")
    public ApiResponse<InvoiceItemResponse> updateInvoiceItem(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceItemUpdateRequest request) {
        InvoiceItemEntity entity = invoiceItemMapper.toEntity(request);
        InvoiceItemEntity updated = invoiceItemService.updateInvoiceItem(id, entity);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Invoice item updated successfully");
    }

    /**
     * Delete invoice item
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInvoiceItem(@PathVariable Long id) {
        invoiceItemService.deleteInvoiceItem(id);
        return ApiResponse.success(null, "Invoice item deleted successfully");
    }

    /**
     * Get invoice items by invoice
     */
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<InvoiceItemResponse>> getInvoiceItemsByInvoice(@PathVariable Long invoiceId) {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getInvoiceItemsByInvoice(invoiceId));
        return ApiResponse.success(invoiceItems, "Invoice items retrieved successfully");
    }

    /**
     * Get invoice items by dish
     */
    @GetMapping("/dish/{dishId}")
    public ApiResponse<List<InvoiceItemResponse>> getInvoiceItemsByDish(@PathVariable Long dishId) {
        List<InvoiceItemResponse> invoiceItems = invoiceItemMapper
                .toResponseList(invoiceItemService.getInvoiceItemsByDish(dishId));
        return ApiResponse.success(invoiceItems, "Invoice items retrieved successfully");
    }

    /**
     * Update invoice item quantity
     */
    @PatchMapping("/{id}/quantity")
    public ApiResponse<InvoiceItemResponse> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        InvoiceItemEntity updated = invoiceItemService.updateQuantity(id, quantity);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Invoice item quantity updated successfully");
    }

    /**
     * Add item to invoice
     */
    @PostMapping("/add-to-invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<InvoiceItemResponse> addItemToInvoice(
            @RequestParam Long invoiceId,
            @RequestParam Long dishId,
            @RequestParam Integer quantity) {
        InvoiceItemEntity created = invoiceItemService.addItemToInvoice(invoiceId, dishId, quantity);
        return ApiResponse.success(invoiceItemMapper.toResponse(created), "Item added to invoice successfully");
    }

    /**
     * Update status of invoice item
     * Example:
     * PUT /api/invoice-items/2/status?status=PREPARING
     */
    @PutMapping("/{id}/status")
    public ApiResponse<InvoiceItemResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam InvoiceItemStatus status) {
        InvoiceItemEntity updated = invoiceItemService.updateStatus(id, status);
        return ApiResponse.success(invoiceItemMapper.toResponse(updated), "Invoice item status updated successfully");
    }
}
