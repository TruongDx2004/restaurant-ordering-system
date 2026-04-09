package restaurant.project.order_table.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.auth.LoginRequest;
import restaurant.project.order_table.dto.request.auth.RefreshTokenRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.auth.LoginResponse;
import restaurant.project.order_table.service.AuthService;

/**
 * POST /api/auth/login          – đăng nhập admin/nhân viên
 * POST /api/auth/refresh-token  – lấy access token mới từ refresh token
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /** Đăng nhập – trả về accessToken, refreshToken và thông tin user */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Login successful");
    }

    /**
     * Làm mới access token.
     * Body: { "refreshToken": "..." }
     */
    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(
                authService.refreshToken(request.getRefreshToken()),
                "Access token mới đã được tạo");
    }
}
