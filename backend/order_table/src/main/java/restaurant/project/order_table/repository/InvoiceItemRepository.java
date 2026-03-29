package restaurant.project.order_table.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import restaurant.project.order_table.entity.InvoiceItemEntity;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long> {

    List<InvoiceItemEntity> findByInvoiceId(Long invoiceId);

    List<InvoiceItemEntity> findByDishId(Long dishId);

    @Query("SELECT SUM(i.totalPrice) FROM InvoiceItemEntity i WHERE i.invoice.id = :invoiceId AND i.status <> restaurant.project.order_table.entity.enums.InvoiceItemStatus.CANCELLED")
    BigDecimal calculateTotalExcludingCancelled(@Param("invoiceId") Long invoiceId);
}

