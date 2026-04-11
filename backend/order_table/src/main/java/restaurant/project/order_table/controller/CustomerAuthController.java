package restaurant.project.order_table.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.customer.CustomerLoginRequest;
import restaurant.project.order_table.dto.request.customer.CustomerRegisterRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.TokenResponse;
import restaurant.project.order_table.dto.response.customer.CustomerRegisterResponse;
import restaurant.project.order_table.entity.CustomerEntity;
import restaurant.project.order_table.mapper.CustomerMapper;
import restaurant.project.order_table.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerRegisterResponse> register(@Valid @RequestBody CustomerRegisterRequest request) {
        CustomerEntity customer = customerService.register(
                request.getFullName(),
                request.getPhone(),
                request.getPassword()
        );
        CustomerRegisterResponse response = customerMapper.toRegisterResponse(customer);
        return ApiResponse.success(response, "Khách hàng đã được đăng ký thành công");
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody CustomerLoginRequest request) {
        String token = customerService.login(request.getPhone(), request.getPassword());
        TokenResponse response = customerMapper.toTokenResponse(token);
        return ApiResponse.success(response, "Đăng nhập thành công");
    }
}
