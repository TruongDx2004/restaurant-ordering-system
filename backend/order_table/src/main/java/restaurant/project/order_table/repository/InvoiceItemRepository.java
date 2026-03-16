package restaurant.project.order_table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.InvoiceItemEntity;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long> {

    List<InvoiceItemEntity> findByInvoiceId(Long invoiceId);
    
    List<InvoiceItemEntity> findByDishId(Long dishId);
}
