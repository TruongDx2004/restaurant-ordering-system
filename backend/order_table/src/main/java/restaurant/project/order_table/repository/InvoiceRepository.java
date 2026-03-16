package restaurant.project.order_table.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.InvoiceEntity;
import restaurant.project.order_table.entity.enums.InvoiceStatus;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    List<InvoiceEntity> findByStatus(InvoiceStatus status);
    
    List<InvoiceEntity> findByTableId(Long tableId);
    
    List<InvoiceEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InvoiceEntity> findByTableIdAndStatus(Long tableId, InvoiceStatus status);
}
