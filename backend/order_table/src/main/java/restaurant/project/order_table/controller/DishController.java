package restaurant.project.order_table.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.dish.DishCreateRequest;
import restaurant.project.order_table.dto.request.dish.DishUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.dish.DishResponse;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.enums.DishStatus;
import restaurant.project.order_table.mapper.DishMapper;
import restaurant.project.order_table.service.DishService;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishMapper dishMapper;

    /**
     * Create a new dish
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DishResponse> createDish(@Valid @RequestBody DishCreateRequest request) {
        DishEntity entity = dishMapper.toEntity(request);
        DishEntity created = dishService.createDish(entity);
        return ApiResponse.success(dishMapper.toResponse(created), "Dish created successfully");
    }

    /**
     * Get dish by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<DishResponse> getDishById(@PathVariable Long id) {
        DishEntity dish = dishService.getDishById(id);
        return ApiResponse.success(dishMapper.toResponse(dish), "Dish retrieved successfully");
    }

    /**
     * Get all dishes
     */
    @GetMapping
    public ApiResponse<List<DishResponse>> getAllDishes() {
        List<DishResponse> dishes = dishService.getAllDishes().stream()
                .map(dishMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(dishes, "Dishes retrieved successfully");
    }

    /**
     * Update dish
     */
    @PutMapping("/{id}")
    public ApiResponse<DishResponse> updateDish(
            @PathVariable Long id,
            @Valid @RequestBody DishUpdateRequest request) {
        DishEntity entity = dishMapper.toEntity(request);
        DishEntity updated = dishService.updateDish(id, entity);
        return ApiResponse.success(dishMapper.toResponse(updated), "Dish updated successfully");
    }

    /**
     * Delete dish
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ApiResponse.success(null, "Dish deleted successfully");
    }

    /**
     * Get dishes by status
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<DishResponse>> getDishesByStatus(@PathVariable DishStatus status) {
        List<DishResponse> dishes = dishService.getDishesByStatus(status).stream()
                .map(dishMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(dishes, "Dishes retrieved successfully");
    }

    /**
     * Get dishes by category
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<DishResponse>> getDishesByCategory(@PathVariable Long categoryId) {
        List<DishResponse> dishes = dishService.getDishesByCategory(categoryId).stream()
                .map(dishMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(dishes, "Dishes retrieved successfully");
    }

    /**
     * Search dishes by name
     */
    @GetMapping("/search")
    public ApiResponse<List<DishResponse>> searchDishesByName(@RequestParam String name) {
        List<DishResponse> dishes = dishService.searchDishesByName(name).stream()
                .map(dishMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(dishes, "Dishes retrieved successfully");
    }

    /**
     * Update dish status
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<DishResponse> updateDishStatus(
            @PathVariable Long id,
            @RequestParam DishStatus status) {
        DishEntity updated = dishService.updateDishStatus(id, status);
        return ApiResponse.success(dishMapper.toResponse(updated), "Dish status updated successfully");
    }
}
