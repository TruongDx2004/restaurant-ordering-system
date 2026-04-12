package restaurant.project.order_table.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.CustomerEntity;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.CustomerRepository;
import restaurant.project.order_table.service.CustomerService;
import restaurant.project.order_table.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public CustomerEntity register(String fullName, String phone, String rawPassword) {
        customerRepository.findByPhone(phone)
                .ifPresent(c -> {
                    throw new BadRequestException("Số điện thoại đã được đăng ký");
                });

        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(fullName);
        customer.setPhone(phone);
        customer.setPassword(passwordEncoder.encode(rawPassword));
        customer.setStatus("ACTIVE");

        return customerRepository.save(customer);
    }

    @Override
    public String login(String phone, String rawPassword) {
        CustomerEntity customer = customerRepository
                .findByPhone(phone)
                .orElseThrow(() -> new BadRequestException("Số điện thoại hoặc mật khẩu không đúng"));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new BadRequestException("Tài khoản khách hàng không hoạt động");
        }

        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new BadRequestException("Số điện thoại hoặc mật khẩu không đúng");
        }

        // Generate JWT token
        return jwtUtil.generateToken(customer.getId(), customer.getPhone());
    }
}
