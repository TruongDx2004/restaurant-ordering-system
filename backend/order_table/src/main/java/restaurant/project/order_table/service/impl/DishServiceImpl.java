package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.dish.*;
import restaurant.project.order_table.entity.CategoryEntity;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.enums.DishStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.DishRepository;
import restaurant.project.order_table.service.CategoryService;
import restaurant.project.order_table.service.DishService;
import restaurant.project.order_table.util.FileUtil;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final CategoryService categoryService;

    @Override
    public DishEntity createDish(DishCreateRequest request) {

        CategoryEntity category = categoryService.getCategoryById(request.getCategoryId());

        DishEntity dish = DishEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .category(category)
                .build();

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = FileUtil.saveFile(request.getImage());
            dish.setImage("/uploads/" + fileName);
        }

        return dishRepository.save(dish);
    }

    @Override
    public DishEntity getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy món ăn với id: " + id));
    }

    @Override
    public List<DishEntity> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public DishEntity updateDish(Long id, DishUpdateRequest request) {

        DishEntity existing = getDishById(id);

        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setStatus(request.getStatus());

        CategoryEntity category = categoryService.getCategoryById(request.getCategoryId());
        existing.setCategory(category);
        
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = FileUtil.saveFile(request.getImage());
            existing.setImage("/uploads/" + fileName);
        }

        return dishRepository.save(existing);
    }

    @Override
    public void deleteDish(Long id) {
        DishEntity dish = getDishById(id);
        dishRepository.delete(dish);
    }

    @Override
    public List<DishEntity> getDishesByStatus(DishStatus status) {
        return dishRepository.findByStatus(status);
    }

    @Override
    public List<DishEntity> getDishesByCategory(Long categoryId) {
        return dishRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<DishEntity> searchDishesByName(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public DishEntity updateDishStatus(Long id, DishStatus status) {
        DishEntity dish = getDishById(id);
        dish.setStatus(status);
        return dishRepository.save(dish);
    }
}
