package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.response.TokenResponse;
import restaurant.project.order_table.dto.response.customer.CustomerRegisterResponse;
import restaurant.project.order_table.entity.CustomerEntity;
import restaurant.project.order_table.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final JwtUtil jwtUtil;

    public CustomerRegisterResponse toRegisterResponse(CustomerEntity entity) {
        CustomerRegisterResponse response = new CustomerRegisterResponse();
        response.setId(entity.getId());
        response.setFullName(entity.getFullName());
        response.setPhone(entity.getPhone());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setMessage("Customer registered successfully");
        return response;
    }

    public TokenResponse toTokenResponse(String token) {
        return new TokenResponse(
                token,
                "Bearer",
                jwtUtil.getExpiration() / 1000
        );
    }
}
