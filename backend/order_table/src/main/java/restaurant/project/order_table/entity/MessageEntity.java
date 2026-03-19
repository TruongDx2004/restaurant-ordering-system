package restaurant.project.order_table.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import restaurant.project.order_table.entity.enums.MessageType;
import restaurant.project.order_table.entity.enums.MessageSender;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageid")
    private Long id;

    /* Nội dung text */
    @Column(columnDefinition = "TEXT")
    private String content;

    /* Loại tin nhắn */
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    /* Người gửi */
    @Enumerated(EnumType.STRING)
    @Column(name = "sender", nullable = false)
    private MessageSender sender;

    /* Thời gian tạo */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /* ====== RELATION ====== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceid")
    private InvoiceEntity invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableid")
    private TableEntity table;

    //OnCreate
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
