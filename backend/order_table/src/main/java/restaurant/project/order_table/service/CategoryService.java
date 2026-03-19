package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.CategoryEntity;

public interface CategoryService {

    /**
     * Create a new category
     *
     * @param category category data
     * @return created category
     */
    CategoryEntity createCategory(CategoryEntity category);

    /**
     * Get category by ID
     *
     * @param id category ID
     * @return category entity
     */
    CategoryEntity getCategoryById(Long id);

    /**
     * Get all categories
     *
     * @return list of all categories
     */
    List<CategoryEntity> getAllCategories();

    /**
     * Update category
     *
     * @param id category ID
     * @param category updated category data
     * @return updated category
     */
    CategoryEntity updateCategory(Long id, CategoryEntity category);

    /**
     * Delete category
     *
     * @param id category ID
     */
    void deleteCategory(Long id);

    /**
     * Find category by name
     *
     * @param name category name
     * @return category entity
     */
    CategoryEntity getCategoryByName(String name);
}
