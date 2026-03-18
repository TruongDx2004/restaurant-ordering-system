package restaurant.project.order_table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByInvoiceId(Long invoiceId);
    
    Optional<PaymentEntity> findByTransactionCode(String transactionCode);
    
    List<PaymentEntity> findByStatus(PaymentStatus status);
    
    List<PaymentEntity> findByMethod(PaymentMethod method);
}
