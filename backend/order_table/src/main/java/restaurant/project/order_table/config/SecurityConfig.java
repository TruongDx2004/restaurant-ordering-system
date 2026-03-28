package restaurant.project.order_table.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.security.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// Bật cấu hình CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource))

				// Tắt CSRF vì ta dùng JWT
				.csrf(csrf -> csrf.disable())

				// Không dùng session để lưu trữ thông tin người dùng
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Cấu hình phân quyền truy cập
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								// Allow auth endpoints
								"/api/auth/**",
								"/api/customers/register",
								"/api/customers/login",
								// Allow customer to create invoice
								"/api/invoices/create-with-items",
								// Allow getting dishes and categories
								"/api/dishes/**",
								"/api/categories/**",
								// Swagger UI endpoints
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/api/paymentsonline/momo/ipn",
								"/api/paymentsonline/momo/callback",
								"/api/paymentsonline/momo/**")
						.permitAll()
						// Cho phép tất cả các đường dẫn liên quan đến WebSocket - QUAN TRỌNG!
						.requestMatchers("/ws/**").permitAll()
						.requestMatchers("/ws").permitAll()
						.requestMatchers("/app/**").permitAll()
						.requestMatchers("/topic/**").permitAll()
						.requestMatchers("/queue/**").permitAll()
						.requestMatchers("/user/**").permitAll()
						.anyRequest().authenticated())

				// Thêm bộ lọc JWT vào trước bộ lọc xác thực của Spring Security
				.addFilterBefore(
						jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
