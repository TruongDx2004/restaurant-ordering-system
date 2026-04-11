package restaurant.project.order_table.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.category.CategoryCreateRequest;
import restaurant.project.order_table.dto.request.category.CategoryUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.category.CategoryResponse;
import restaurant.project.order_table.entity.CategoryEntity;
import restaurant.project.order_table.mapper.CategoryMapper;
import restaurant.project.order_table.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        CategoryEntity created = categoryService.createCategory(entity);
        return ApiResponse.success(categoryMapper.toResponse(created), "Tạo danh mục thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryEntity category = categoryService.getCategoryById(id);
        return ApiResponse.success(categoryMapper.toResponse(category), "Danh mục đã được lấy thành công");
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(categories, "Danh mục đã được lấy thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        CategoryEntity updated = categoryService.updateCategory(id, entity);
        return ApiResponse.success(categoryMapper.toResponse(updated), "Danh mục đã được cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success(null, "Danh mục đã được xóa thành công");
    }

    @GetMapping("/name/{name}")
    public ApiResponse<CategoryResponse> getCategoryByName(@PathVariable String name) {
        CategoryEntity category = categoryService.getCategoryByName(name);
        return ApiResponse.success(categoryMapper.toResponse(category), "Danh mục đã được lấy thành công");
    }
}
