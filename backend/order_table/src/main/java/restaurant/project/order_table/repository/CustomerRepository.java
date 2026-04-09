package restaurant.project.order_table.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByPhone(String phone);
}
