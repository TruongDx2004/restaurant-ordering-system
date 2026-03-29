/**
 * WebSocket Service
 * Quản lý kết nối WebSocket cho real-time updates sử dụng STOMP protocol đơn giản
 */

class WebSocketService {
  constructor() {
    this.socket = null;
    this.connected = false;
    this.subscriptions = new Map(); // topic -> callback
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectDelay = 5000;
    this.baseUrl = import.meta.env.VITE_API_BASE_URL 
      ? import.meta.env.VITE_API_BASE_URL.replace('/api', '/ws').replace('http', 'ws')
      : 'ws://localhost:8080/ws';
  }

  /**
   * Kết nối đến WebSocket server
   */
  connect() {
    if (this.connected || this.socket) return;

    console.log('[WebSocket] Connecting to:', this.baseUrl);
    this.socket = new WebSocket(this.baseUrl);

    this.socket.onopen = () => {
      console.log('[WebSocket] Connected');
      this.connected = true;
      this.reconnectAttempts = 0;
      
      // Gửi STOMP CONNECT frame
      this._sendFrame('CONNECT', { 'accept-version': '1.1,1.0', 'heart-beat': '10000,10000' });
    };

    this.socket.onmessage = (event) => {
      this._handleFrame(event.data);
    };

    this.socket.onclose = () => {
      console.log('[WebSocket] Disconnected');
      this.connected = false;
      this.socket = null;
      
      // Auto reconnect
      if (this.reconnectAttempts < this.maxReconnectAttempts) {
        this.reconnectAttempts++;
        setTimeout(() => this.connect(), this.reconnectDelay);
      }
    };

    this.socket.onerror = (error) => {
      console.error('[WebSocket] Error:', error);
    };
  }

  /**
   * Subscribe vào một topic
   * @param {string} topic - Ví dụ: /topic/orders/status
   * @param {function} callback - Hàm xử lý khi có tin nhắn
   */
  subscribe(topic, callback) {
    if (!this.subscriptions.has(topic)) {
      this.subscriptions.set(topic, []);
    }
    this.subscriptions.get(topic).push(callback);

    // Nếu đã connect, gửi SUBSCRIBE frame ngay
    if (this.connected) {
      this._sendFrame('SUBSCRIBE', { destination: topic, id: topic });
    }

    // Trả về hàm để unsubscribe
    return () => this.unsubscribe(topic, callback);
  }

  /**
   * Hủy subscribe
   */
  unsubscribe(topic, callback) {
    if (this.subscriptions.has(topic)) {
      const callbacks = this.subscriptions.get(topic);
      const index = callbacks.indexOf(callback);
      if (index > -1) {
        callbacks.splice(index, 1);
      }
      
      if (callbacks.length === 0) {
        this.subscriptions.delete(topic);
        if (this.connected) {
          this._sendFrame('UNSUBSCRIBE', { id: topic });
        }
      }
    }
  }

  /**
   * Gửi tin nhắn đến server
   */
  send(destination, body) {
    if (this.connected) {
      this._sendFrame('SEND', { destination }, JSON.stringify(body));
    } else {
      console.warn('[WebSocket] Cannot send message, not connected');
    }
  }

  /**
   * Gửi STOMP Frame
   */
  _sendFrame(command, headers, body = '') {
    let frame = command + '\n';
    for (const key in headers) {
      frame += key + ':' + headers[key] + '\n';
    }
    frame += '\n' + body + '\0';
    
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(frame);
    }
  }

  /**
   * Xử lý STOMP Frame nhận được
   */
  _handleFrame(data) {
    // Basic STOMP parser
    const parts = data.split('\n\n');
    const headerLines = parts[0].split('\n');
    const command = headerLines[0];
    const headers = {};
    
    for (let i = 1; i < headerLines.length; i++) {
      const [key, value] = headerLines[i].split(':');
      if (key) headers[key] = value;
    }

    if (command === 'CONNECTED') {
      console.log('[WebSocket] STOMP Protocol Handshake complete');
      // Resubscribe all existing subscriptions on reconnect
      this.subscriptions.forEach((_, topic) => {
        this._sendFrame('SUBSCRIBE', { destination: topic, id: topic });
      });
    } else if (command === 'MESSAGE') {
      const body = parts[1].replace(/\0$/, '');
      const destination = headers['destination'];
      
      try {
        const payload = JSON.parse(body);
        if (this.subscriptions.has(destination)) {
          this.subscriptions.get(destination).forEach(cb => cb(payload));
        }
      } catch (e) {
        console.error('[WebSocket] Error parsing message body:', e);
      }
    }
  }
}

export const webSocketService = new WebSocketService();
export default webSocketService;
