package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.enums.DishStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.DishRepository;
import restaurant.project.order_table.service.DishService;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Override
    public DishEntity createDish(DishEntity dish) {
        return dishRepository.save(dish);
    }

    @Override
    public DishEntity getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Dish not found with id: " + id));
    }

    @Override
    public List<DishEntity> getAllDishes() {
        return dishRepository.findAll();
    }

    @Override
    public DishEntity updateDish(Long id, DishEntity dish) {
        DishEntity existingDish = getDishById(id);

        existingDish.setName(dish.getName());
        existingDish.setPrice(dish.getPrice());
        existingDish.setImage(dish.getImage());
        existingDish.setStatus(dish.getStatus());
        existingDish.setCategory(dish.getCategory());

        return dishRepository.save(existingDish);
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
