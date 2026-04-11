package restaurant.project.order_table.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRegisterRequest {

    @NotBlank(message = "Full name không được để trống")
    private String fullName;

    @NotBlank(message = "Phone number không được để trống")
    @Pattern(
        regexp = "^(0[0-9]{9})$",
        message = "Số điện thoại phải bắt đầu bằng số 0 và có tổng cộng 10 chữ số"
    )
    private String phone;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 kí tự")
    private String password;
}
