package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.dish.DishCreateRequest;
import restaurant.project.order_table.dto.request.dish.DishUpdateRequest;
import restaurant.project.order_table.dto.response.dish.DishResponse;
import restaurant.project.order_table.entity.CategoryEntity;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.service.CategoryService;

@Component
@RequiredArgsConstructor
public class DishMapper {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public DishEntity toEntity(DishCreateRequest request) {
        CategoryEntity category = categoryService.getCategoryById(request.getCategoryId());
        
        return DishEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .category(category)
                .build();
    }

    public DishEntity toEntity(DishUpdateRequest request) {
        CategoryEntity category = categoryService.getCategoryById(request.getCategoryId());
        
        return DishEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .category(category)
                .build();
    }

    public DishResponse toResponse(DishEntity entity) {
        return DishResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .image(entity.getImage())
                .category(categoryMapper.toResponse(entity.getCategory()))
                .build();
    }
}
