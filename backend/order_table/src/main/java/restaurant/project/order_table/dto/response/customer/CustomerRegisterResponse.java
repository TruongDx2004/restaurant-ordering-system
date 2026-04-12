package restaurant.project.order_table.dto.response.customer;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CustomerRegisterResponse {

	private Long id;

	private String fullName;

	private String phone;

	private String status;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;

	private String message;
}
