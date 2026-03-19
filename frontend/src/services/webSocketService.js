/**
 * WebSocket Service
 * Quản lý kết nối WebSocket cho real-time updates
 * 
 * TODO: Implement WebSocket connection
 * - Connect to WebSocket server
 * - Handle events: new-order, order-status-update, payment-notification
 * - Emit events from client
 * - Auto reconnect on disconnect
 */

class WebSocketService {
  constructor() {
    this.socket = null;
    this.listeners = new Map();
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectDelay = 3000;
  }

  /**
   * Kết nối đến WebSocket server
   * @param {string} url - WebSocket server URL
   */
  connect(url) {
    // TODO: Implement WebSocket connection
    console.log('[WebSocket] Ready to connect to:', url);
    
    // Example structure:
    // this.socket = new WebSocket(url);
    // this.socket.onopen = this.handleOpen.bind(this);
    // this.socket.onmessage = this.handleMessage.bind(this);
    // this.socket.onerror = this.handleError.bind(this);
    // this.socket.onclose = this.handleClose.bind(this);
  }

  /**
   * Ngắt kết nối WebSocket
   */
  disconnect() {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  /**
   * Emit event đến server
   * @param {string} event - Event name
   * @param {object} data - Event data
   */
  emit(event, data) {
    // TODO: Implement emit
    console.log('[WebSocket] Would emit:', event, data);
    
    // if (this.socket && this.socket.readyState === WebSocket.OPEN) {
    //   this.socket.send(JSON.stringify({ event, data }));
    // }
  }

  /**
   * Lắng nghe event từ server
   * @param {string} event - Event name
   * @param {function} callback - Callback function
   */
  on(event, callback) {
    if (!this.listeners.has(event)) {
      this.listeners.set(event, []);
    }
    this.listeners.get(event).push(callback);
  }

  /**
   * Xóa listener
   * @param {string} event - Event name
   * @param {function} callback - Callback function
   */
  off(event, callback) {
    if (this.listeners.has(event)) {
      const callbacks = this.listeners.get(event);
      const index = callbacks.indexOf(callback);
      if (index > -1) {
        callbacks.splice(index, 1);
      }
    }
  }

  /**
   * Handle incoming messages
   * @param {MessageEvent} message - WebSocket message
   */
  handleMessage(message) {
    try {
      const { event, data } = JSON.parse(message.data);
      
      // Trigger listeners
      if (this.listeners.has(event)) {
        this.listeners.get(event).forEach(callback => callback(data));
      }
    } catch (error) {
      console.error('[WebSocket] Error parsing message:', error);
    }
  }

  /**
   * Handle connection open
   */
  handleOpen() {
    console.log('[WebSocket] Connected');
    this.reconnectAttempts = 0;
  }

  /**
   * Handle connection error
   */
  handleError(error) {
    console.error('[WebSocket] Error:', error);
  }

  /**
   * Handle connection close
   */
  handleClose() {
    console.log('[WebSocket] Disconnected');
    
    // Auto reconnect
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      setTimeout(() => {
        console.log(`[WebSocket] Reconnecting... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
        // this.connect(this.url);
      }, this.reconnectDelay);
    }
  }
}

// Export singleton instance
export const webSocketService = new WebSocketService();
export default webSocketService;
