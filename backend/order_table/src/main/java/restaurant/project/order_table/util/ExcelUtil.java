package restaurant.project.order_table.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import restaurant.project.order_table.entity.*;
import restaurant.project.order_table.entity.enums.DishStatus;
import restaurant.project.order_table.entity.enums.Role;
import restaurant.project.order_table.entity.enums.TableStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    // ================= EXPORT =================
    public static ByteArrayInputStream exportCategories(List<CategoryEntity> categories) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Categories");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name"};
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (CategoryEntity category : categories) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(category.getId() != null ? category.getId() : 0);
                row.createCell(1).setCellValue(category.getName());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi export Category ra Excel: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream exportDishes(List<DishEntity> dishes) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Dishes");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Price", "Status", "Image", "Category ID"};
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (DishEntity dish : dishes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dish.getId() != null ? dish.getId() : 0);
                row.createCell(1).setCellValue(dish.getName());
                row.createCell(2).setCellValue(dish.getPrice());
                row.createCell(3).setCellValue(dish.getStatus() != null ? dish.getStatus().name() : "");
                row.createCell(4).setCellValue(dish.getImage() != null ? dish.getImage() : "");
                row.createCell(5).setCellValue(dish.getCategory() != null ? dish.getCategory().getId() : 0);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi export Dish ra Excel: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream exportUsers(List<UserEntity> users) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Email", "Name", "Phone", "Role"};
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (UserEntity user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getId() != null ? user.getId() : 0);
                row.createCell(1).setCellValue(user.getEmail());
                row.createCell(2).setCellValue(user.getName() != null ? user.getName() : "");
                row.createCell(3).setCellValue(user.getPhone() != null ? user.getPhone() : "");
                row.createCell(4).setCellValue(user.getRole() != null ? user.getRole().name() : "");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi export User ra Excel: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream exportTables(List<TableEntity> tables) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Tables");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Table Number", "Area", "Status", "Is Active"};
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (TableEntity table : tables) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(table.getId() != null ? table.getId() : 0);
                row.createCell(1).setCellValue(table.getTableNumber() != null ? table.getTableNumber() : 0);
                row.createCell(2).setCellValue(table.getArea());
                row.createCell(3).setCellValue(table.getStatus() != null ? table.getStatus().name() : "");
                row.createCell(4).setCellValue(table.getIsActive() != null ? table.getIsActive() : false);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi export Table ra Excel: " + e.getMessage());
        }
    }

    // ================= IMPORT =================
    public static List<CategoryEntity> importCategories(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<CategoryEntity> categories = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                CategoryEntity category = new CategoryEntity();
                Cell nameCell = currentRow.getCell(1);
                if (nameCell != null) {
                    category.setName(nameCell.getStringCellValue());
                }

                categories.add(category);
            }
            return categories;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi parse Excel file Category: " + e.getMessage());
        }
    }

    public static List<DishEntity> importDishes(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<DishEntity> dishes = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Bỏ qua header
                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                // Kiểm tra dòng rỗng
                String nameCheck = formatter.formatCellValue(currentRow.getCell(1)).trim();
                if (nameCheck.isEmpty() || nameCheck.contains("---")) {
                    continue;
                }

                DishEntity dish = new DishEntity();
                try {
                    dish.setName(nameCheck);

                    String priceStr = formatter.formatCellValue(currentRow.getCell(2)).replaceAll("[^0-9]", "");
                    dish.setPrice(!priceStr.isEmpty() ? Integer.parseInt(priceStr) : 0);

                    String statusStr = formatter.formatCellValue(currentRow.getCell(3)).trim().toUpperCase();
                    if (!statusStr.isEmpty() && !statusStr.equals("---")) {
                        dish.setStatus(DishStatus.valueOf(statusStr));
                    } else {
                        dish.setStatus(DishStatus.AVAILABLE);
                    }

                    dish.setImage(formatter.formatCellValue(currentRow.getCell(4)).trim());

                    String catIdStr = formatter.formatCellValue(currentRow.getCell(5)).replaceAll("[^0-9]", "");
                    if (!catIdStr.isEmpty()) {
                        CategoryEntity category = new CategoryEntity();
                        category.setId(Long.parseLong(catIdStr));
                        dish.setCategory(category);
                    }

                    dishes.add(dish);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Lỗi dữ liệu tại dòng " + (currentRow.getRowNum() + 1) + " (Món: " + nameCheck + "). Vui lòng kiểm tra lại Trạng thái hoặc Giá.");
                }
            }
            return dishes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<UserEntity> importUsers(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<UserEntity> users = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                UserEntity user = new UserEntity();

                if (currentRow.getCell(1) != null) {
                    user.setEmail(currentRow.getCell(1).getStringCellValue());
                }
                if (currentRow.getCell(2) != null) {
                    user.setPassword(currentRow.getCell(2).getStringCellValue());

                }
                if (currentRow.getCell(3) != null) {
                    user.setName(currentRow.getCell(3).getStringCellValue());
                }

                if (currentRow.getCell(4) != null) {
                    Cell phoneCell = currentRow.getCell(4);
                    if (phoneCell.getCellType() == CellType.NUMERIC) {
                        user.setPhone(String.valueOf((long) phoneCell.getNumericCellValue()));
                    } else {
                        user.setPhone(phoneCell.getStringCellValue());
                    }
                }

                if (currentRow.getCell(5) != null) {
                    user.setRole(Role.valueOf(currentRow.getCell(5).getStringCellValue().toUpperCase()));
                }

                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi parse Excel file User: " + e.getMessage());
        }
    }

    public static List<TableEntity> importTables(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<TableEntity> tables = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                String tableNumCheck = formatter.formatCellValue(currentRow.getCell(1)).trim();
                if (tableNumCheck.isEmpty() || tableNumCheck.contains("---")) {
                    continue;
                }

                TableEntity table = new TableEntity();

                try {
                    table.setTableNumber(Integer.parseInt(tableNumCheck));

                    table.setArea(formatter.formatCellValue(currentRow.getCell(2)).trim());

                    String statusStr = formatter.formatCellValue(currentRow.getCell(3)).trim().toUpperCase();
                    if (!statusStr.isEmpty() && !statusStr.equals("---")) {
                        table.setStatus(TableStatus.valueOf(statusStr));
                    } else {
                        table.setStatus(TableStatus.AVAILABLE);
                    }

                    String isActiveStr = formatter.formatCellValue(currentRow.getCell(4)).trim();
                    table.setIsActive(Boolean.parseBoolean(isActiveStr));

                    tables.add(table);

                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Lỗi dữ liệu tại dòng số " + (currentRow.getRowNum() + 1) + " trong Excel. Vui lòng kiểm tra lại Số bàn hoặc Trạng thái (Chỉ chấp nhận AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE).");
                }
            }
            return tables;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
