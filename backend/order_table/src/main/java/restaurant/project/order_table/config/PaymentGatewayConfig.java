package restaurant.project.order_table.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Config for external payment gateways (MoMo, VNPay)
 */
@Configuration
public class PaymentGatewayConfig {

	/**
	 * RestTemplate dùng để gọi API ra ngoài (MoMo, VNPay)
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				// ✅ ĐÚNG với Spring Boot 3.2.x
				.setConnectTimeout(Duration.ofSeconds(10))
				.setReadTimeout(Duration.ofSeconds(30))
				.build();
	}

	/**
	 * ObjectMapper để parse JSON từ payment gateway
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				// hỗ trợ LocalDateTime
				.registerModule(new JavaTimeModule())
				// tránh crash khi gateway trả field lạ
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
}