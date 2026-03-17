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
        // Check phone number already exists
        customerRepository.findByPhone(phone)
                .ifPresent(c -> {
                    throw new BadRequestException("Phone number already registered");
                });

        // Create new customer entity
        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(fullName);
        customer.setPhone(phone);
        customer.setPassword(passwordEncoder.encode(rawPassword));
        customer.setStatus("ACTIVE");

        // Save to database
        return customerRepository.save(customer);
    }

    @Override
    public String login(String phone, String rawPassword) {
        CustomerEntity customer = customerRepository
                .findByPhone(phone)
                .orElseThrow(() -> new BadRequestException("Invalid phone or password"));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new BadRequestException("Customer account is not active");
        }

        if (!passwordEncoder.matches(rawPassword, customer.getPassword())) {
            throw new BadRequestException("Invalid phone or password");
        }

        // Generate JWT token
        return jwtUtil.generateToken(customer.getId(), customer.getPhone());
    }
}
