package restaurant.project.order_table.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerLoginRequest {

    @NotBlank(message = "Phone không được để trống")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    private String password;
}
