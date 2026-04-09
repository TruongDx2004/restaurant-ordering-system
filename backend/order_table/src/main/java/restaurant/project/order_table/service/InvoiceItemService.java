package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;

public interface InvoiceItemService {

    /**
     * Create a new invoice item
     *
     * @param invoiceItem invoice item data
     * @return created invoice item
     */
    InvoiceItemEntity createInvoiceItem(InvoiceItemEntity invoiceItem);

    /**
     * Get invoice item by ID
     *
     * @param id invoice item ID
     * @return invoice item entity
     */
    InvoiceItemEntity getInvoiceItemById(Long id);

    /**
     * Get all invoice items
     *
     * @return list of all invoice items
     */
    List<InvoiceItemEntity> getAllInvoiceItems();

    /**
     * Update invoice item
     *
     * @param id invoice item ID
     * @param invoiceItem updated invoice item data
     * @return updated invoice item
     */
    InvoiceItemEntity updateInvoiceItem(Long id, InvoiceItemEntity invoiceItem);

    /**
     * Delete invoice item
     *
     * @param id invoice item ID
     */
    void deleteInvoiceItem(Long id);

    /**
     * Get invoice items by invoice
     *
     * @param invoiceId invoice ID
     * @return list of invoice items
     */
    List<InvoiceItemEntity> getInvoiceItemsByInvoice(Long invoiceId);

    /**
     * Get invoice items by dish
     *
     * @param dishId dish ID
     * @return list of invoice items
     */
    List<InvoiceItemEntity> getInvoiceItemsByDish(Long dishId);

    /**
     * Update invoice item quantity
     *
     * @param id invoice item ID
     * @param quantity new quantity
     * @return updated invoice item
     */
    InvoiceItemEntity updateQuantity(Long id, Integer quantity);

    /**
     * Add item to invoice
     *
     * @param invoiceId invoice ID
     * @param dishId dish ID
     * @param quantity quantity
     * @return created invoice item
     */
    InvoiceItemEntity addItemToInvoice(Long invoiceId, Long dishId, Integer quantity);

    /**
     * Update invoice item status
     *
     * @param id invoice item ID
     * @param status new status
     * @return updated invoice item
     */
    InvoiceItemEntity updateStatus(Long id, InvoiceItemStatus status);
}
