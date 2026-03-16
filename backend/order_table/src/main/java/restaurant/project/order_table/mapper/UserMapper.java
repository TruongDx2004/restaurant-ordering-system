package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;
import restaurant.project.order_table.dto.request.user.UserCreateRequest;
import restaurant.project.order_table.dto.request.user.UserUpdateRequest;
import restaurant.project.order_table.dto.response.user.UserResponse;
import restaurant.project.order_table.entity.UserEntity;

@Component
public class UserMapper {

    public UserEntity toEntity(UserCreateRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
    }

    public UserEntity toEntity(UserUpdateRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
    }

    public UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
