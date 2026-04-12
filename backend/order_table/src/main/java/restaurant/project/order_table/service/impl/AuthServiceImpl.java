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
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Email hoặc mật khẩu không đúng"));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE) {
            throw new BadRequestException("Truy câp bị từ chối");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email hoặc mật khẩu không đúng");
        }

        String token = generateTokenForUser(user);
        String refreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(String refreshToken) {

        UserEntity user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Yêu cầu không hợp lệ"));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE) {
            throw new BadRequestException("Truy câp bị từ chối");
        }

        String newToken = generateTokenForUser(user);
        String newRefreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

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
                .orElseThrow(() -> new BadRequestException("Người dùng không tồn tại"));

        user.setRefreshToken(null);
        userRepository.save(user);
    }

    private String generateTokenForUser(UserEntity user) {
        return jwtUtil.generateToken(user.getId(), user.getEmail());
    }
}
