package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.enums.DishStatus;

public interface DishService {

    /**
     * Create a new dish
     *
     * @param dish dish data
     * @return created dish
     */
    DishEntity createDish(DishEntity dish);

    /**
     * Get dish by ID
     *
     * @param id dish ID
     * @return dish entity
     */
    DishEntity getDishById(Long id);

    /**
     * Get all dishes
     *
     * @return list of all dishes
     */
    List<DishEntity> getAllDishes();

    /**
     * Update dish
     *
     * @param id dish ID
     * @param dish updated dish data
     * @return updated dish
     */
    DishEntity updateDish(Long id, DishEntity dish);

    /**
     * Delete dish
     *
     * @param id dish ID
     */
    void deleteDish(Long id);

    /**
     * Get dishes by status
     *
     * @param status dish status
     * @return list of dishes
     */
    List<DishEntity> getDishesByStatus(DishStatus status);

    /**
     * Get dishes by category
     *
     * @param categoryId category ID
     * @return list of dishes
     */
    List<DishEntity> getDishesByCategory(Long categoryId);

    /**
     * Search dishes by name
     *
     * @param name dish name keyword
     * @return list of dishes
     */
    List<DishEntity> searchDishesByName(String name);

    /**
     * Update dish status
     *
     * @param id dish ID
     * @param status new status
     * @return updated dish
     */
    DishEntity updateDishStatus(Long id, DishStatus status);
}
