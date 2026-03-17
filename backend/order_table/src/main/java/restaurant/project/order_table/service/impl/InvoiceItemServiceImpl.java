package restaurant.project.order_table.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.InvoiceItemEntity;
import restaurant.project.order_table.entity.enums.InvoiceItemStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.InvoiceItemRepository;
import restaurant.project.order_table.service.DishService;
import restaurant.project.order_table.service.InvoiceItemService;
import restaurant.project.order_table.service.InvoiceService;

@Service
@RequiredArgsConstructor
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceService invoiceService;
    private final DishService dishService;

    @Override
    public InvoiceItemEntity createInvoiceItem(InvoiceItemEntity invoiceItem) {
        if (invoiceItem.getStatus() == null)
            invoiceItem.setStatus(InvoiceItemStatus.WAITING);
        return invoiceItemRepository.save(invoiceItem);
    }

    @Override
    public InvoiceItemEntity getInvoiceItemById(Long id) {
        return invoiceItemRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Invoice item not found with id: " + id));
    }

    @Override
    public List<InvoiceItemEntity> getAllInvoiceItems() {
        return invoiceItemRepository.findAll();
    }

    @Override
    public InvoiceItemEntity updateInvoiceItem(Long id, InvoiceItemEntity invoiceItem) {
        InvoiceItemEntity existingItem = getInvoiceItemById(id);

        existingItem.setDish(invoiceItem.getDish());
        existingItem.setQuantity(invoiceItem.getQuantity());
        existingItem.setUnitPrice(invoiceItem.getUnitPrice());
        if (invoiceItem.getStatus() != null) {
            existingItem.setStatus(invoiceItem.getStatus());
        }
        // Recalculate total price
        BigDecimal totalPrice = invoiceItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(invoiceItem.getQuantity()));
        existingItem.setTotalPrice(totalPrice);

        return invoiceItemRepository.save(existingItem);
    }

    @Override
    public void deleteInvoiceItem(Long id) {
        InvoiceItemEntity invoiceItem = getInvoiceItemById(id);
        invoiceItemRepository.delete(invoiceItem);
    }

    @Override
    public List<InvoiceItemEntity> getInvoiceItemsByInvoice(Long invoiceId) {
        return invoiceItemRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public List<InvoiceItemEntity> getInvoiceItemsByDish(Long dishId) {
        return invoiceItemRepository.findByDishId(dishId);
    }

    @Override
    public InvoiceItemEntity updateQuantity(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        InvoiceItemEntity invoiceItem = getInvoiceItemById(id);
        invoiceItem.setQuantity(quantity);
        return invoiceItemRepository.save(invoiceItem);
    }

    @Override
    public InvoiceItemEntity addItemToInvoice(Long invoiceId, Long dishId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        InvoiceEntity invoice = invoiceService.getInvoiceById(invoiceId);
        DishEntity dish = dishService.getDishById(dishId);

        // Get unit price from dish
        BigDecimal unitPrice = BigDecimal.valueOf(dish.getPrice());
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

        InvoiceItemEntity invoiceItem = new InvoiceItemEntity();
        invoiceItem.setInvoice(invoice);
        invoiceItem.setDish(dish);
        invoiceItem.setQuantity(quantity);
        invoiceItem.setUnitPrice(unitPrice);
        invoiceItem.setTotalPrice(totalPrice);

        return invoiceItemRepository.save(invoiceItem);
    }

    @Override
    public InvoiceItemEntity updateStatus(Long id, InvoiceItemStatus status) {
        InvoiceItemEntity item = getInvoiceItemById(id);

        // ❗ Validate flow (rất quan trọng)
        if (item.getStatus() == InvoiceItemStatus.SERVED) {
            throw new BadRequestException("Cannot update status of a served item");
        }

        if (item.getStatus() == InvoiceItemStatus.CANCELLED) {
            throw new BadRequestException("Cannot update status of a cancelled item");
        }

        item.setStatus(status);
        item.setUpdatedAt(LocalDateTime.now());

        return invoiceItemRepository.save(item);
    }
}
