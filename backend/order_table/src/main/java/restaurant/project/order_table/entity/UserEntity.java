package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import restaurant.project.order_table.entity.enums.Role;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email của người dùng */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /** Mật khẩu */
    @Column(name = "password", nullable = false)
    private String password;

    /** Tên người dùng */
    @Column(name = "name", length = 100)
    private String name;

    /** Số điện thoại */
    @Column(name = "phone", length = 255)
    private String phone;

    /** Vai trò của người dùng */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    /** Refresh Token */
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    /** Thời gian tạo và cập nhật */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
