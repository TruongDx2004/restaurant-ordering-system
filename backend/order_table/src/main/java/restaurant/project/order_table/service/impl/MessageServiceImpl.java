package restaurant.project.order_table.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.response.message.ConversationResponse;
import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.MessageRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.MessageService;
import restaurant.project.order_table.service.TableService;
import restaurant.project.order_table.websocket.WebSocketService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TableService tableService;
    private final TableRepository tableRepository;
    private final WebSocketService webSocketService;

    @Override
    public MessageEntity createMessage(MessageEntity message) {
        MessageEntity saved = messageRepository.save(message);

        if (saved.getSender() == MessageSender.CUSTOMER) {
            webSocketService.sendMessageFromCustomer(
                    saved.getTable().getId(),
                    saved.getSender(),
                    saved.getContent());
        } else if (saved.getSender() == MessageSender.STAFF) {
            webSocketService.sendMessageToTable(
                    saved.getTable().getId(),
                    saved.getSender(),
                    saved.getContent());
        } else if (saved.getSender() == MessageSender.SYSTEM) {
            Long tableId = saved.getTable() != null ? saved.getTable().getId() : null;
            webSocketService.sendGlobalNotification(
                    "SYSTEM_MESSAGE",
                    saved.getContent(),
                    tableId != null ? java.util.Map.of("tableId", tableId) : null);
        }

        return saved;
    }

    @Override
    public MessageEntity getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tin nhắn không tồn tại với id: " + id));
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public MessageEntity updateMessage(Long id, MessageEntity message) {
        MessageEntity existing = getMessageById(id);
        existing.setContent(message.getContent());
        existing.setMessageType(message.getMessageType());
        existing.setSender(message.getSender());
        existing.setInvoice(message.getInvoice());
        existing.setTable(message.getTable());
        return messageRepository.save(existing);
    }

    @Override
    public void deleteMessage(Long id) {
        MessageEntity message = getMessageById(id);
        messageRepository.delete(message);
    }

    @Override
    public List<MessageEntity> getMessagesByInvoice(Long invoiceId) {
        return messageRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public List<MessageEntity> getMessagesByTable(Long tableId) {
        return messageRepository.findByTableId(tableId);
    }

    @Override
    public List<MessageEntity> getMessagesByType(MessageType messageType) {
        return messageRepository.findByMessageType(messageType);
    }

    @Override
    public List<MessageEntity> getMessagesBySender(MessageSender sender) {
        return messageRepository.findBySender(sender);
    }

    @Override
    public List<MessageEntity> getMessagesByTableOrderedByDate(Long tableId) {
        return messageRepository.findByTableIdOrderByCreatedAtDesc(tableId);
    }

    @Override
    public MessageEntity sendMessageToTable(Long tableId, String content, MessageType messageType, MessageSender sender) {
        TableEntity table = tableService.getTableById(tableId);

        MessageEntity message = new MessageEntity();
        message.setTable(table);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setSender(sender);

        return messageRepository.save(message);
    }

    @Override
    public List<ConversationResponse> getConversations() {
        List<TableEntity> tables = tableRepository.findAll();
        tables.sort(Comparator.comparing(
                TableEntity::getTableNumber,
                Comparator.nullsLast(Comparator.naturalOrder())));

        return tables.stream().map(table -> {
            Optional<MessageEntity> lastMsg =
                    messageRepository.findTopByTableIdOrderByCreatedAtDesc(table.getId());

            return ConversationResponse.builder()
                    .id(table.getId())
                    .tableNumber(table.getTableNumber())
                    .status(table.getStatus())
                    .sender(lastMsg.map(MessageEntity::getSender).orElse(null))
                    .lastMessage(lastMsg.map(MessageEntity::getContent).orElse("Chưa có tin nhắn"))
                    .lastTime(lastMsg.map(MessageEntity::getCreatedAt).orElse(null))
                    .unreadCount(0)
                    .build();
        }).collect(Collectors.toList());
    }
}
