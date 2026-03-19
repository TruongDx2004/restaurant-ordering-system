package restaurant.project.order_table.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.message.MessageCreateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.message.MessageResponse;
import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;
import restaurant.project.order_table.mapper.MessageMapper;
import restaurant.project.order_table.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    /**
     * Create a new message
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MessageResponse> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        MessageEntity entity = messageMapper.toEntity(request);
        MessageEntity created = messageService.createMessage(entity);
        return ApiResponse.success(messageMapper.toResponse(created), "Message created successfully");
    }

    /**
     * Get message by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<MessageResponse> getMessageById(@PathVariable Long id) {
        MessageEntity message = messageService.getMessageById(id);
        return ApiResponse.success(messageMapper.toResponse(message), "Message retrieved successfully");
    }

    /**
     * Get all messages
     */
    @GetMapping
    public ApiResponse<List<MessageResponse>> getAllMessages() {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getAllMessages());
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Update message
     */
    @PutMapping("/{id}")
    public ApiResponse<MessageResponse> updateMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageCreateRequest request) {
        MessageEntity entity = messageMapper.toEntity(request);
        MessageEntity updated = messageService.updateMessage(id, entity);
        return ApiResponse.success(messageMapper.toResponse(updated), "Message updated successfully");
    }

    /**
     * Delete message
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ApiResponse.success(null, "Message deleted successfully");
    }

    /**
     * Get messages by invoice
     */
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<MessageResponse>> getMessagesByInvoice(@PathVariable Long invoiceId) {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getMessagesByInvoice(invoiceId));
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Get messages by table
     */
    @GetMapping("/table/{tableId}")
    public ApiResponse<List<MessageResponse>> getMessagesByTable(@PathVariable Long tableId) {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getMessagesByTable(tableId));
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Get messages by type
     */
    @GetMapping("/type/{messageType}")
    public ApiResponse<List<MessageResponse>> getMessagesByType(@PathVariable MessageType messageType) {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getMessagesByType(messageType));
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Get messages by sender
     */
    @GetMapping("/sender/{sender}")
    public ApiResponse<List<MessageResponse>> getMessagesBySender(@PathVariable MessageSender sender) {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getMessagesBySender(sender));
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Get messages by table ordered by date
     */
    @GetMapping("/table/{tableId}/ordered")
    public ApiResponse<List<MessageResponse>> getMessagesByTableOrderedByDate(@PathVariable Long tableId) {
        List<MessageResponse> messages = messageMapper.toResponseList(messageService.getMessagesByTableOrderedByDate(tableId));
        return ApiResponse.success(messages, "Messages retrieved successfully");
    }

    /**
     * Send message to table
     */
    @PostMapping("/send-to-table")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MessageResponse> sendMessageToTable(
            @RequestParam Long tableId,
            @RequestParam String content,
            @RequestParam MessageType messageType,
            @RequestParam MessageSender sender) {
        MessageEntity created = messageService.sendMessageToTable(tableId, content, messageType, sender);
        return ApiResponse.success(messageMapper.toResponse(created), "Message sent successfully");
    }
}
