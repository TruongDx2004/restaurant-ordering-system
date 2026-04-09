package restaurant.project.order_table.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Cấu hình các nguồn gốc được phép (origins)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // React default
                "http://localhost:4200",      // Angular default
                "http://localhost:5173",      // Vite default
                "http://localhost:8081",      // Alternative port
                "http://127.0.0.1:3000",
                "http://127.0.0.1:5500",
                "http://127.0.0.1:5501",
                "http://127.0.0.1:8080",
                "http://127.0.0.1:4200",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:8081"
        ));
        
        // Chấp nhận các phương thức HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Chấp nhận tất cả các headers từ client
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Cho phép gửi cookie, authorization headers, v.v.
        configuration.setAllowCredentials(true);
        
        // Các headers được phép hiển thị cho client
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        
        // Thời gian sống của cấu hình CORS (trong giây)
        configuration.setMaxAge(3600L);
        
        // Ánh xạ cấu hình CORS cho tất cả các đường dẫn
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
