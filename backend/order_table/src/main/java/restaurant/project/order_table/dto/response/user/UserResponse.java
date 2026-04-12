package restaurant.project.order_table.dto.response.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurant.project.order_table.entity.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
}
