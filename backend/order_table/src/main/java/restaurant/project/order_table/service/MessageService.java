package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.MessageEntity;
import restaurant.project.order_table.entity.enums.MessageSender;
import restaurant.project.order_table.entity.enums.MessageType;

public interface MessageService {

    /**
     * Create a new message
     *
     * @param message message data
     * @return created message
     */
    MessageEntity createMessage(MessageEntity message);

    /**
     * Get message by ID
     *
     * @param id message ID
     * @return message entity
     */
    MessageEntity getMessageById(Long id);

    /**
     * Get all messages
     *
     * @return list of all messages
     */
    List<MessageEntity> getAllMessages();

    /**
     * Update message
     *
     * @param id message ID
     * @param message updated message data
     * @return updated message
     */
    MessageEntity updateMessage(Long id, MessageEntity message);

    /**
     * Delete message
     *
     * @param id message ID
     */
    void deleteMessage(Long id);

    /**
     * Get messages by invoice
     *
     * @param invoiceId invoice ID
     * @return list of messages
     */
    List<MessageEntity> getMessagesByInvoice(Long invoiceId);

    /**
     * Get messages by table
     *
     * @param tableId table ID
     * @return list of messages
     */
    List<MessageEntity> getMessagesByTable(Long tableId);

    /**
     * Get messages by type
     *
     * @param messageType message type
     * @return list of messages
     */
    List<MessageEntity> getMessagesByType(MessageType messageType);

    /**
     * Get messages by sender
     *
     * @param sender message sender
     * @return list of messages
     */
    List<MessageEntity> getMessagesBySender(MessageSender sender);

    /**
     * Get messages by table ordered by creation date
     *
     * @param tableId table ID
     * @return list of messages ordered by date descending
     */
    List<MessageEntity> getMessagesByTableOrderedByDate(Long tableId);

    /**
     * Send message to table
     *
     * @param tableId table ID
     * @param content message content
     * @param messageType message type
     * @param sender message sender
     * @return created message
     */
    MessageEntity sendMessageToTable(Long tableId, String content, MessageType messageType, MessageSender sender);
}
