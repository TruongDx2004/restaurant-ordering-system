package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.entity.enums.Role;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.UserRepository;
import restaurant.project.order_table.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity createUser(UserEntity user) {
        // Check if email already exists
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new BadRequestException("Email already exists");
                });

        // Check if phone already exists
        userRepository.findByPhone(user.getPhone())
                .ifPresent(u -> {
                    throw new BadRequestException("Phone number already exists");
                });

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found with id: " + id));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity user) {
        UserEntity existingUser = getUserById(id);

        // Check if new email conflicts with another user
        if (!existingUser.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(user.getEmail())
                    .ifPresent(u -> {
                        throw new BadRequestException("Email already exists");
                    });
        }

        // Check if new phone conflicts with another user
        if (!existingUser.getPhone().equals(user.getPhone())) {
            userRepository.findByPhone(user.getPhone())
                    .ifPresent(u -> {
                        throw new BadRequestException("Phone number already exists");
                    });
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));
    }

    @Override
    public UserEntity getUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new BadRequestException("User not found with phone: " + phone));
    }

    @Override
    public List<UserEntity> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public UserEntity updatePassword(Long id, String newPassword) {
        UserEntity user = getUserById(id);
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateRefreshToken(Long id, String refreshToken) {
        UserEntity user = getUserById(id);
        user.setRefreshToken(refreshToken);
        return userRepository.save(user);
    }
}
