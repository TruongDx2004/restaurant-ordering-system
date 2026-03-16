package restaurant.project.order_table.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.project.order_table.dto.request.auth.LoginRequest;
import restaurant.project.order_table.dto.response.auth.LoginResponse;
import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.entity.enums.Role;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.mapper.UserMapper;
import restaurant.project.order_table.repository.UserRepository;
import restaurant.project.order_table.service.AuthService;
import restaurant.project.order_table.util.JwtUtil;

import java.util.UUID;

/**
 * Implementation of AuthService
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by email
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        // Verify user is ADMIN or EMPLOYEE
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE) {
            throw new BadRequestException("Access denied. Admin or Employee role required.");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Generate tokens
        String token = generateTokenForUser(user);
        String refreshToken = UUID.randomUUID().toString();

        // Save refresh token
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Build response
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        // Find user by refresh token
        UserEntity user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        // Verify user is ADMIN or EMPLOYEE
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE) {
            throw new BadRequestException("Access denied");
        }

        // Generate new tokens
        String newToken = generateTokenForUser(user);
        String newRefreshToken = UUID.randomUUID().toString();

        // Update refresh token
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        // Build response
        return LoginResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Clear refresh token
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    /**
     * Generate JWT token for user
     * Note: JwtUtil currently only has generateToken(Long customerId, String phone)
     * We'll use userId and email for admin/employee
     */
    private String generateTokenForUser(UserEntity user) {
        // Using existing JwtUtil method - pass userId as customerId and email as phone
        // This is a workaround. Ideally, JwtUtil should have a method for admin users
        return jwtUtil.generateToken(user.getId(), user.getEmail());
    }
}
