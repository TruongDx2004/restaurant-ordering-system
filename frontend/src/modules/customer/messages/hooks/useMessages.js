import { useState, useEffect, useCallback, useRef } from 'react';
import { messageApi } from '../../../../api';

/**
 * Custom hook for customer messaging
 * Synchronized with backend MessageType and MessageSender
 * @param {string|number} tableNumber 
 * @param {number|null} invoiceId 
 * @param {number} refreshInterval - Default 0 (polling disabled for future WebSocket)
 */
export const useMessages = (tableNumber, invoiceId, refreshInterval = 0) => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sending, setSending] = useState(false);

  /**
   * Fetch messages based on invoiceId (if exists) or tableNumber
   */
  const fetchMessages = useCallback(async () => {
    if (!tableNumber) return;

    // Requirement: Only show messages if there is an active invoice
    if (!invoiceId) {
      setMessages([]);
      setLoading(false);
      return;
    }

    try {
      const response = await messageApi.getByInvoice(invoiceId);
      
      // The response structure from ApiResponse is { success, data, message }
      if (response && response.success) {
        setMessages(response.data || []);
      } else if (Array.isArray(response)) {
        // Fallback if the interceptor returns only data
        setMessages(response);
      }
    } catch (err) {
      console.error('Error fetching messages:', err);
      setError('Không thể tải tin nhắn');
    } finally {
      setLoading(false);
    }
  }, [tableNumber, invoiceId]);

  // Initial fetch
  useEffect(() => {
    fetchMessages();
  }, [fetchMessages]);

  // Polling (only if interval > 0)
  useEffect(() => {
    if (refreshInterval > 0 && tableNumber) {
      const interval = setInterval(fetchMessages, refreshInterval);
      return () => clearInterval(interval);
    }
  }, [fetchMessages, refreshInterval, tableNumber]);

  /**
   * Send a new message
   * @param {string} content 
   * @param {string} type - Matches backend MessageType: TEXT, IMAGE, QUICK_ACTION, CALL_WAITER, SYSTEM, REQUEST_BILL
   */
  const sendMessage = async (content, type = 'TEXT') => {
    if (!content.trim() && type === 'TEXT') return;
    if (!tableNumber) return;

    setSending(true);
    try {
      const messageData = {
        tableId: parseInt(tableNumber),
        invoiceId: invoiceId ? parseInt(invoiceId) : null,
        content: content,
        messageType: type,
        sender: 'CUSTOMER' // Matches backend MessageSender: CUSTOMER
      };

      const response = await messageApi.create(messageData);
      
      // From MessageController.createMessage returns ApiResponse<MessageResponse>
      if (response && response.success && response.data) {
        setMessages(prev => [...prev, response.data]);
        return { success: true };
      } else if (response && response.id) {
        // Fallback if data is returned directly
        setMessages(prev => [...prev, response]);
        return { success: true };
      }
      
      return { success: false, error: 'Phản hồi không hợp lệ từ máy chủ' };
    } catch (err) {
      console.error('Error sending message:', err);
      return { success: false, error: 'Gửi tin nhắn thất bại' };
    } finally {
      setSending(false);
    }
  };

  /**
   * Quick action: Call waiter
   */
  const callStaff = async () => {
    return sendMessage('Yêu cầu nhân viên đến bàn', 'CALL_WAITER');
  };

  /**
   * Quick action: Request bill
   */
  const requestBill = async () => {
    return sendMessage('Yêu cầu thanh toán hóa đơn', 'REQUEST_BILL');
  };

  return {
    messages,
    loading,
    error,
    sending,
    sendMessage,
    callStaff,
    requestBill,
    refetch: fetchMessages
  };
};

export default useMessages;
