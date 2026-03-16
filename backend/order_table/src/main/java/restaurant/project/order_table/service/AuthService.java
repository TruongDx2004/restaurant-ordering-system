package restaurant.project.order_table.service;

import restaurant.project.order_table.dto.request.auth.LoginRequest;
import restaurant.project.order_table.dto.response.auth.LoginResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Authenticate admin/employee user
     *
     * @param loginRequest login credentials
     * @return login response with token and user info
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * Refresh access token using refresh token
     *
     * @param refreshToken refresh token
     * @return new login response with new tokens
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * Logout user by invalidating refresh token
     *
     * @param userId user ID
     */
    void logout(Long userId);
}
