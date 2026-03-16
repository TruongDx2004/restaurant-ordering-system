package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;
import restaurant.project.order_table.dto.request.category.CategoryCreateRequest;
import restaurant.project.order_table.dto.request.category.CategoryUpdateRequest;
import restaurant.project.order_table.dto.response.category.CategoryResponse;
import restaurant.project.order_table.entity.CategoryEntity;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CategoryCreateRequest request) {
        return CategoryEntity.builder()
                .name(request.getName())
                .build();
    }

    public CategoryEntity toEntity(CategoryUpdateRequest request) {
        return CategoryEntity.builder()
                .name(request.getName())
                .build();
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
