package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import restaurant.project.order_table.entity.enums.RecipientType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tiêu đề */
    @Column(nullable = false)
    private String title;

    /** Nội dung thông báo */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /** Loại sự kiện WebSocket (e.g. "NEW_ORDER", "PAYMENT_SUCCESS") */
    @Column(nullable = false)
    private String type;

    /** USER | ROLE | ALL */
    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type")
    @Builder.Default
    private RecipientType recipientType = RecipientType.ALL;

    /** ID người nhận (null = broadcast cho tất cả loại recipientType đó) */
    @Column(name = "recipient_id")
    private Long recipientId;

    /** Đã đọc hay chưa */
    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private Boolean read = false;

    /** Dữ liệu kèm theo (lưu dạng JSON string) */
    @Column(columnDefinition = "TEXT")
    private String data;

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
