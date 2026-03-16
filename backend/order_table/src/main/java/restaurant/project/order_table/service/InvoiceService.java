package restaurant.project.order_table.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.enums.InvoiceStatus;

public interface InvoiceService {

    /**
     * Create a new invoice
     *
     * @param invoice invoice data
     * @return created invoice
     */
    InvoiceEntity createInvoice(InvoiceEntity invoice);

    /**
     * Get invoice by ID
     *
     * @param id invoice ID
     * @return invoice entity
     */
    InvoiceEntity getInvoiceById(Long id);

    /**
     * Get all invoices
     *
     * @return list of all invoices
     */
    List<InvoiceEntity> getAllInvoices();

    /**
     * Update invoice
     *
     * @param id invoice ID
     * @param invoice updated invoice data
     * @return updated invoice
     */
    InvoiceEntity updateInvoice(Long id, InvoiceEntity invoice);

    /**
     * Delete invoice
     *
     * @param id invoice ID
     */
    void deleteInvoice(Long id);

    /**
     * Get invoices by status
     *
     * @param status invoice status
     * @return list of invoices
     */
    List<InvoiceEntity> getInvoicesByStatus(InvoiceStatus status);

    /**
     * Get invoices by table
     *
     * @param tableId table ID
     * @return list of invoices
     */
    List<InvoiceEntity> getInvoicesByTable(Long tableId);

    /**
     * Get invoices by date range
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of invoices
     */
    List<InvoiceEntity> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Update invoice status
     *
     * @param id invoice ID
     * @param status new status
     * @return updated invoice
     */
    InvoiceEntity updateInvoiceStatus(Long id, InvoiceStatus status);

    /**
     * Get active invoice for a table
     *
     * @param tableId table ID
     * @return invoice entity or null
     */
    InvoiceEntity getActiveInvoiceByTable(Long tableId);

    /**
     * Get active invoice for a table by table number
     *
     * @param tableNumber table number
     * @return invoice entity or null
     */
    InvoiceEntity getActiveInvoiceByTableNumber(Integer tableNumber);

    /**
     * Calculate invoice total
     *
     * @param id invoice ID
     * @return calculated total amount
     */
    BigDecimal calculateInvoiceTotal(Long id);

    /**
     * Create invoice with items (for customer order)
     *
     * @param tableId table ID
     * @param items list of items (dishId, quantity, notes)
     * @return created invoice with items
     */
    InvoiceEntity createInvoiceWithItems(Long tableId, List<ItemData> items);

    /**
     * Item data for creating invoice
     */
    class ItemData {
        public Long dishId;
        public Integer quantity;
        public String notes;

        public ItemData(Long dishId, Integer quantity, String notes) {
            this.dishId = dishId;
            this.quantity = quantity;
            this.notes = notes;
        }
    }
}
