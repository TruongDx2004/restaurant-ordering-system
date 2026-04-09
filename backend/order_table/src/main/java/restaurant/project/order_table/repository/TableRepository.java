package restaurant.project.order_table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.TableStatus;

public interface TableRepository extends JpaRepository<TableEntity, Long> {

    Optional<TableEntity> findByTableNumber(Integer tableNumber);
    
    List<TableEntity> findByStatus(TableStatus status);
    
    List<TableEntity> findByArea(String area);
    
    List<TableEntity> findByIsActive(Boolean isActive);
}
