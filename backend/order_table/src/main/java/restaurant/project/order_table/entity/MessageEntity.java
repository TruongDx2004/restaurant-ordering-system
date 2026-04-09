package restaurant.project.order_table.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;

@Entity
@Table(name = "messages")
@SQLDelete(sql = "UPDATE messages SET deleted_at = NOW() WHERE messageid = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageid")
    private Long id;

    /** Nội dung text */
    @Column(columnDefinition = "TEXT")
    private String content;

    /** Loại tin nhắn */
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    /** Người gửi */
    @Enumerated(EnumType.STRING)
    @Column(name = "sender", nullable = false)
    private MessageSender sender;

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

    /* ====== RELATION ====== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceid")
    private InvoiceEntity invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableid")
    private TableEntity table;
}
