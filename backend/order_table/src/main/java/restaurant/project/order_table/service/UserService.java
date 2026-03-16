package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.entity.enums.Role;

public interface UserService {

    /**
     * Create a new user
     *
     * @param user user data
     * @return created user
     */
    UserEntity createUser(UserEntity user);

    /**
     * Get user by ID
     *
     * @param id user ID
     * @return user entity
     */
    UserEntity getUserById(Long id);

    /**
     * Get all users
     *
     * @return list of all users
     */
    List<UserEntity> getAllUsers();

    /**
     * Update user
     *
     * @param id user ID
     * @param user updated user data
     * @return updated user
     */
    UserEntity updateUser(Long id, UserEntity user);

    /**
     * Delete user
     *
     * @param id user ID
     */
    void deleteUser(Long id);

    /**
     * Get user by email
     *
     * @param email user email
     * @return user entity
     */
    UserEntity getUserByEmail(String email);

    /**
     * Get user by phone
     *
     * @param phone user phone
     * @return user entity
     */
    UserEntity getUserByPhone(String phone);

    /**
     * Get users by role
     *
     * @param role user role
     * @return list of users
     */
    List<UserEntity> getUsersByRole(Role role);

    /**
     * Update user password
     *
     * @param id user ID
     * @param newPassword new password (already encoded)
     * @return updated user
     */
    UserEntity updatePassword(Long id, String newPassword);

    /**
     * Update refresh token
     *
     * @param id user ID
     * @param refreshToken refresh token
     * @return updated user
     */
    UserEntity updateRefreshToken(Long id, String refreshToken);
}
