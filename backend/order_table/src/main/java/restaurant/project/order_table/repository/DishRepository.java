package restaurant.project.order_table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.enums.DishStatus;

public interface DishRepository extends JpaRepository<DishEntity, Long> {

    List<DishEntity> findByStatus(DishStatus status);
    
    List<DishEntity> findByCategoryId(Long categoryId);
    
    List<DishEntity> findByNameContainingIgnoreCase(String name);
}
