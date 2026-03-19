package restaurant.project.order_table.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // cấu hình route tin nhắn
        config.enableSimpleBroker("/topic", "/queue");
        
        // Cấu hình prefix cho các tin nhắn gửi từ client lên server
        config.setApplicationDestinationPrefixes("/app");
        
        // Cấu hình prefix cho các tin nhắn gửi đến người dùng cụ thể
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đăng ký endpoint cho kết nối WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Cho phép tất cả các nguồn gốc
                .withSockJS();  // Bật SockJS fallback
        
        // Đăng ký thêm endpoint không dùng SockJS (nếu cần)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
