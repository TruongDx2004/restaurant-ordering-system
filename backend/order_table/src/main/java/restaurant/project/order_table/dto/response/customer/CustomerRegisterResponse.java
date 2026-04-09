package restaurant.project.order_table.dto.response.customer;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerRegisterResponse {

	private Long id;

	private String fullName;

	private String phone;

	private String status;

	private LocalDateTime createdAt;

	private String message;
}
