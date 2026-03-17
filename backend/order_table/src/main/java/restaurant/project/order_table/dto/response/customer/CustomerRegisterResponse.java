package restaurant.project.order_table.dto.response.customer;

import java.util.Date;
import lombok.Data;

@Data
public class CustomerRegisterResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String status;

    private Date createdAt;

    private String message;
}
