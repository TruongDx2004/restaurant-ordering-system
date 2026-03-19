package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "customer")
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

    /** Trạng thái */
    @Column(name = "status", nullable = false)
    private String status;

    /** Ngày tạo */
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    //Constructors
    public CustomerEntity() {
        this.createdAt = new Date();
    }

}