package restaurant.project.order_table.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import restaurant.project.order_table.entity.CategoryEntity;
import restaurant.project.order_table.entity.DishEntity;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.UserEntity;
import restaurant.project.order_table.repository.CategoryRepository;
import restaurant.project.order_table.repository.DishRepository;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.repository.UserRepository;
import restaurant.project.order_table.service.ExcelService;
import restaurant.project.order_table.util.ExcelUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final TableRepository tableRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ByteArrayInputStream exportData(String entityType) {
        return switch (entityType.toLowerCase()) {
            case "category" ->
                ExcelUtil.exportCategories(categoryRepository.findAll());
            case "dish" ->
                ExcelUtil.exportDishes(dishRepository.findAll());
            case "user" ->
                ExcelUtil.exportUsers(userRepository.findAll());
            case "table" ->
                ExcelUtil.exportTables(tableRepository.findAll());
            default ->
                throw new IllegalArgumentException("Không hỗ trợ export entity: " + entityType);
        };
    }

    @Override
    @Transactional
    public void importData(String entityType, MultipartFile file) {
        try {
            switch (entityType.toLowerCase()) {
                case "category":
                    List<CategoryEntity> categories = ExcelUtil.importCategories(file.getInputStream());
                    categoryRepository.saveAll(categories);
                    break;
                case "dish":
                    List<DishEntity> dishes = ExcelUtil.importDishes(file.getInputStream());
                    dishRepository.saveAll(dishes);
                    break;
                case "user":
                    List<UserEntity> users = ExcelUtil.importUsers(file.getInputStream());
                    // Mã hóa mật khẩu trước khi lưu
                    users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
                    userRepository.saveAll(users);
                    break;
                case "table":
                    List<TableEntity> tables = ExcelUtil.importTables(file.getInputStream());
                    tableRepository.saveAll(tables);
                    break;
                default:
                    throw new IllegalArgumentException("Không hỗ trợ import entity: " + entityType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage());
        }
    }
}
