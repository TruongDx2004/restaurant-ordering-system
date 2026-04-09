package restaurant.project.order_table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.entity.enums.Role;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByPhone(String phone);
    
    List<UserEntity> findByRole(Role role);
    
    Optional<UserEntity> findByRefreshToken(String refreshToken);
}
