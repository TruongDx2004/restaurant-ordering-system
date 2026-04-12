package restaurant.project.order_table.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.user.UserCreateRequest;
import restaurant.project.order_table.dto.request.user.UserUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.user.UserResponse;
import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.mapper.UserMapper;
import restaurant.project.order_table.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserEntity entity = userMapper.toEntity(request);
        UserEntity created = userService.createUser(entity);
        return ApiResponse.success(userMapper.toResponse(created), "Người dùng đã được tạo thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ApiResponse.success(userMapper.toResponse(user), "Người dùng đã được lấy thành công");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.success(users, "Người dùng đã được lấy thành công");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        UserEntity entity = userMapper.toEntity(request);
        UserEntity updated = userService.updateUser(id, entity);

        return ApiResponse.success(userMapper.toResponse(updated), "Người dùng đã được cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null, "Người dùng đã được xóa thành công");
    }
}