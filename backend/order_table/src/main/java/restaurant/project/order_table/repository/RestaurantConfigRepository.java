package restaurant.project.order_table.repository;

import restaurant.project.order_table.entity.RestaurantConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantConfigRepository extends JpaRepository<RestaurantConfigEntity, Long> {
}