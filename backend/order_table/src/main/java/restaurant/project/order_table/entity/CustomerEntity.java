package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@SQLDelete(sql = "UPDATE customer SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Họ và tên */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /** Số điện thoại */
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    /** Mật khẩu */
    @Column(name = "password", nullable = false)
    private String password;

    /** Trạng thái (ACTIVE / INACTIVE) */
    @Column(name = "status", nullable = false)
    private String status;

    /** Thời điểm tạo */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm cập nhật */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Soft delete */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
