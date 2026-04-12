package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.CategoryEntity;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.CategoryRepository;
import restaurant.project.order_table.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        categoryRepository.findByName(category.getName())
                .ifPresent(c -> {
                    throw new BadRequestException("Danh mục đã tồn tại");
                });

        return categoryRepository.save(category);
    }

    @Override
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Danh mục không tồn tại với id: " + id));
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateCategory(Long id, CategoryEntity category) {
        CategoryEntity existingCategory = getCategoryById(id);

        if (!existingCategory.getName().equals(category.getName())) {
            categoryRepository.findByName(category.getName())
                    .ifPresent(c -> {
                        throw new BadRequestException("Danh mục đã tồn tại");
                    });
        }

        existingCategory.setName(category.getName());

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryEntity category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryEntity getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Danh mục không tồn tại với tên: " + name));
    }
}
