package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.MessageRepository;
import restaurant.project.order_table.service.MessageService;
import restaurant.project.order_table.service.TableService;
import restaurant.project.order_table.websocket.WebSocketService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TableService tableService;
    private final WebSocketService webSocketService;
    @Override
    public MessageEntity createMessage(MessageEntity message) {
        
        MessageEntity messageSave = messageRepository.save(message);

        if(messageSave.getSender() == MessageSender.CUSTOMER)
            webSocketService.sendMessageFromCustomer(
                messageSave.getTable().getId(),
                messageSave.getSender(),
                messageSave.getContent()
            );
        else if (messageSave.getSender() == MessageSender.STAFF)
            webSocketService.sendMessageToTable(
                messageSave.getTable().getId(),
                messageSave.getSender(),
                messageSave.getContent()
            );

        return messageSave;
    }

    @Override
    public MessageEntity getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Message not found with id: " + id));
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public MessageEntity updateMessage(Long id, MessageEntity message) {
        MessageEntity existingMessage = getMessageById(id);

        existingMessage.setContent(message.getContent());
        existingMessage.setMessageType(message.getMessageType());
        existingMessage.setSender(message.getSender());
        existingMessage.setInvoice(message.getInvoice());
        existingMessage.setTable(message.getTable());

        return messageRepository.save(existingMessage);
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
}
