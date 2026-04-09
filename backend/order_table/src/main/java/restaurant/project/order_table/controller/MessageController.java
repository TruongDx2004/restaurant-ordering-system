package restaurant.project.order_table.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.message.MessageCreateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.message.ConversationResponse;
import restaurant.project.order_table.dto.response.message.MessageResponse;
import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.mapper.MessageMapper;
import restaurant.project.order_table.service.MessageService;

/**
 * POST   /api/messages                      – gửi tin nhắn mới
 * GET    /api/messages/conversations         – danh sách cuộc trò chuyện theo bàn (STAFF)
 * GET    /api/messages/invoice/{invoiceId}   – tin nhắn theo hóa đơn (STAFF)
 * GET    /api/messages/table/{tableId}       – tin nhắn theo bàn (STAFF)
 * DELETE /api/messages/{id}                  – xóa tin nhắn (STAFF)
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    /** Gửi tin nhắn mới (CUSTOMER hoặc STAFF) */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MessageResponse> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        MessageEntity entity = messageMapper.toEntity(request);
        return ApiResponse.success(
                messageMapper.toResponse(messageService.createMessage(entity)),
                "Tin nhắn được gửi thành công");
    }

    /** Lấy danh sách cuộc trò chuyện – mỗi bàn kèm tin nhắn cuối (STAFF) */
    @GetMapping("/conversations")
    public ApiResponse<List<ConversationResponse>> getConversations() {
        return ApiResponse.success(
                messageService.getConversations(),
                "Cuộc trò chuyện được lấy thành công");
    }

    /** Tin nhắn theo hóa đơn (STAFF) */
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<MessageResponse>> getMessagesByInvoice(@PathVariable Long invoiceId) {
        return ApiResponse.success(
                messageMapper.toResponseList(messageService.getMessagesByInvoice(invoiceId)),
                "Tin nhắn được lấy thành công");
    }

    /** Tin nhắn theo bàn (STAFF) */
    @GetMapping("/table/{tableId}")
    public ApiResponse<List<MessageResponse>> getMessagesByTable(@PathVariable Long tableId) {
        return ApiResponse.success(
                messageMapper.toResponseList(messageService.getMessagesByTable(tableId)),
                "Tin nhắn được lấy thành công");
    }

    /** Xóa tin nhắn (STAFF) */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ApiResponse.success(null, "Tin nhắn được xóa thành công");
    }
}
