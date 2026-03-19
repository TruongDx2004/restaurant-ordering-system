package restaurant.project.order_table.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.auth.LoginRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.auth.LoginResponse;
import restaurant.project.order_table.service.AuthService;

/**
 * REST controller for authentication operations
 * Endpoints for admin and employee login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Admin/Employee login
     *
     * @param loginRequest login credentials
     * @return login response with JWT token and user info
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ApiResponse.success(response, "Login successful");
    }

    /**
     * Refresh access token
     *
     * @param refreshToken refresh token from header
     * @return new login response with new tokens
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.success(response, "Token refreshed successfully");
    }

    /**
     * Logout user
     *
     * @param userId user ID from path
     * @return success response
     */
    @PostMapping("/logout/{userId}")
    public ApiResponse<Void> logout(@PathVariable Long userId) {
        authService.logout(userId);
        return ApiResponse.success(null, "Logout successful");
    }
}
