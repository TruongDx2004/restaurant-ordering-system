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
import java.util.List;
import java.util.function.Function;

public class ExcelUtil {

	public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static boolean hasExcelFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}

	/**
	 * Đọc ô dưới dạng chuỗi, trim, trả "" nếu null. Tương đương getCellValue +
	 * sanitize trong JS.
	 */
	private static String getCellString(DataFormatter formatter, Row row, int col) {
		Cell cell = row.getCell(col);
		if (cell == null)
			return "";
		return formatter.formatCellValue(cell).trim();
	}

	// ================= EXPORT =================

	/**
	 * Columns: ID | Tên danh mục
	 */
	public static ByteArrayInputStream exportCategories(List<CategoryEntity> categories) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("CATEGORY");
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Tên danh mục" };
			for (int col = 0; col < headers.length; col++) {
				headerRow.createCell(col).setCellValue(headers[col]);
			}
			int rowIdx = 1;
			for (CategoryEntity category : categories) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(category.getId() != null ? category.getId() : 0);
				row.createCell(1).setCellValue(category.getName() != null ? category.getName() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Lỗi khi export Category ra Excel: " + e.getMessage());
		}
	}

	/**
	 * Columns: ID | Tên món | Giá | Trạng thái | Tên Danh mục | Link ảnh
	 * Thứ tự cột khớp với JS entityConfigs.dish.columns
	 */
	public static ByteArrayInputStream exportDishes(List<DishEntity> dishes) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("DISH");
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Tên món", "Giá", "Trạng thái", "Tên Danh mục", "Link ảnh" };
			for (int col = 0; col < headers.length; col++) {
				headerRow.createCell(col).setCellValue(headers[col]);
			}

			int rowIdx = 1;
			for (DishEntity dish : dishes) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(dish.getId() != null ? dish.getId() : 0);
				row.createCell(1).setCellValue(dish.getName() != null ? dish.getName() : "");
				row.createCell(2).setCellValue(dish.getPrice());
				row.createCell(3).setCellValue(dish.getStatus() != null ? dish.getStatus().name() : "AVAILABLE");
				row.createCell(4).setCellValue(dish.getCategory() != null ? dish.getCategory().getName() : "");
				row.createCell(5).setCellValue(dish.getImage() != null ? dish.getImage() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Lỗi khi export Dish ra Excel: " + e.getMessage());
		}
	}

	/**
	 * Columns: ID | Email | Họ tên | SĐT | Vai trò
	 */
	public static ByteArrayInputStream exportUsers(List<UserEntity> users) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("USER");
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Email", "Họ tên", "SĐT", "Vai trò" };
			for (int col = 0; col < headers.length; col++) {
				headerRow.createCell(col).setCellValue(headers[col]);
			}

			int rowIdx = 1;
			for (UserEntity user : users) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(user.getId() != null ? user.getId() : 0);
				row.createCell(1).setCellValue(user.getEmail() != null ? user.getEmail() : "");
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

	/**
	 * Columns: ID | Số bàn | Khu vực | Trạng thái | Đang hoạt động
	 */
	public static ByteArrayInputStream exportTables(List<TableEntity> tables) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("TABLE");
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Số bàn", "Khu vực", "Trạng thái", "Đang hoạt động" };
			for (int col = 0; col < headers.length; col++) {
				headerRow.createCell(col).setCellValue(headers[col]);
			}

			int rowIdx = 1;
			for (TableEntity table : tables) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(table.getId() != null ? table.getId() : 0);
				row.createCell(1).setCellValue(table.getTableNumber() != null ? table.getTableNumber() : 0);
				row.createCell(2).setCellValue(table.getArea() != null ? table.getArea() : "");
				row.createCell(3).setCellValue(table.getStatus() != null ? table.getStatus().name() : "AVAILABLE");
				row.createCell(4).setCellValue(table.getIsActive() != null ? table.getIsActive() : false);
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Lỗi khi export Table ra Excel: " + e.getMessage());
		}
	}

	// ================= IMPORT =================

	/**
	 * Import danh mục từ Excel.
	 * Columns (0-indexed): 0=ID(bỏ qua) | 1=Tên danh mục
	 */
	public static List<CategoryEntity> importCategories(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			List<CategoryEntity> categories = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // bỏ header

				String name = getCellString(formatter, row, 1);
				if (name.isEmpty())
					continue; // bỏ dòng rỗng

				CategoryEntity category = new CategoryEntity();
				category.setName(name);
				categories.add(category);
			}
			return categories;
		} catch (IOException e) {
			throw new RuntimeException("Lỗi khi parse Excel file Category: " + e.getMessage());
		}
	}

	/**
	 * Import món ăn từ Excel.
	 * Columns (0-indexed): 0=ID(bỏ qua) | 1=Tên món | 2=Giá | 3=Trạng thái | 4=Tên
	 * Danh mục | 5=Link ảnh
	 *
	 * @param categoryResolver hàm nhận tên danh mục → trả về CategoryEntity
	 *                         (findOrCreate từ DB)
	 */
	public static List<DishEntity> importDishes(InputStream is, Function<String, CategoryEntity> categoryResolver) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			List<DishEntity> dishes = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // bỏ header

				String name = getCellString(formatter, row, 1);
				if (name.isEmpty())
					continue; // bỏ dòng rỗng

				int rowNum = row.getRowNum() + 1;

				// Giá — xóa ký tự không phải số
				String priceStr = getCellString(formatter, row, 2).replaceAll("[^0-9]", "");
				if (priceStr.isEmpty()) {
					throw new RuntimeException("Dòng " + rowNum + ": Giá không hợp lệ (Món: " + name + ")");
				}

				// Trạng thái — mặc định AVAILABLE nếu không hợp lệ
				String statusStr = getCellString(formatter, row, 3).toUpperCase();
				DishStatus status;
				try {
					status = (!statusStr.isEmpty()) ? DishStatus.valueOf(statusStr) : DishStatus.AVAILABLE;
				} catch (IllegalArgumentException e) {
					status = DishStatus.AVAILABLE;
				}

				// Tên danh mục — bắt buộc
				String categoryName = getCellString(formatter, row, 4);
				if (categoryName.isEmpty()) {
					throw new RuntimeException("Dòng " + rowNum + ": Thiếu danh mục (Món: " + name + ")");
				}

				String image = getCellString(formatter, row, 5);

				DishEntity dish = new DishEntity();
				dish.setName(name);
				dish.setPrice(Integer.parseInt(priceStr));
				dish.setStatus(status);
				dish.setImage(image);
				dish.setCategory(categoryResolver.apply(categoryName));

				dishes.add(dish);
			}
			return dishes;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi parse Excel file Dish: " + e.getMessage());
		}
	}

	/**
	 * Import nhân viên từ Excel. Mật khẩu mặc định "123456" (mã hóa ở service
	 * layer).
	 * Columns (0-indexed): 0=ID(bỏ qua) | 1=Email | 2=Họ tên | 3=SĐT | 4=Vai trò
	 */
	public static List<UserEntity> importUsers(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			List<UserEntity> users = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // bỏ header

				String email = getCellString(formatter, row, 1);
				if (email.isEmpty())
					continue; // bỏ dòng rỗng

				String name = getCellString(formatter, row, 2);
				if (name.isEmpty()) {
					throw new RuntimeException("Dòng " + (row.getRowNum() + 1) + ": Thiếu tên (Email: " + email + ")");
				}

				// SĐT — ô số phải đọc dạng số nguyên để tránh định dạng khoa học
				String phone;
				Cell phoneCell = row.getCell(3);
				if (phoneCell != null && phoneCell.getCellType() == CellType.NUMERIC) {
					phone = String.valueOf((long) phoneCell.getNumericCellValue());
				} else {
					phone = getCellString(formatter, row, 3);
				}

				// Vai trò — mặc định EMPLOYEE nếu rỗng hoặc không hợp lệ
				String roleStr = getCellString(formatter, row, 4).toUpperCase();
				Role role;
				try {
					role = (!roleStr.isEmpty()) ? Role.valueOf(roleStr) : Role.EMPLOYEE;
				} catch (IllegalArgumentException e) {
					role = Role.EMPLOYEE;
				}

				UserEntity user = new UserEntity();
				user.setEmail(email);
				user.setName(name);
				user.setPhone(phone);
				user.setRole(role);
				// Mật khẩu mặc định "123456" — sẽ được mã hóa ở ExcelServiceImpl
				user.setPassword("123456");

				users.add(user);
			}
			return users;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi parse Excel file User: " + e.getMessage());
		}
	}

	/**
	 * Import bàn từ Excel.
	 * Columns (0-indexed): 0=ID(bỏ qua) | 1=Số bàn | 2=Khu vực | 3=Trạng thái |
	 * 4=Đang hoạt động
	 */
	public static List<TableEntity> importTables(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			List<TableEntity> tables = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // bỏ header

				String tableNumStr = getCellString(formatter, row, 1);
				if (tableNumStr.isEmpty())
					continue; // bỏ dòng rỗng

				int rowNum = row.getRowNum() + 1;

				int tableNumber;
				try {
					tableNumber = Integer.parseInt(tableNumStr.replaceAll("[^0-9]", ""));
				} catch (NumberFormatException e) {
					throw new RuntimeException("Dòng " + rowNum + ": Số bàn không hợp lệ");
				}

				// Khu vực — mặc định "Khu vực 1" nếu rỗng
				String area = getCellString(formatter, row, 2);
				if (area.isEmpty())
					area = "Khu vực 1";

				// Trạng thái — mặc định AVAILABLE nếu không hợp lệ
				String statusStr = getCellString(formatter, row, 3).toUpperCase();
				TableStatus status;
				try {
					status = (!statusStr.isEmpty()) ? TableStatus.valueOf(statusStr) : TableStatus.AVAILABLE;
				} catch (IllegalArgumentException e) {
					status = TableStatus.AVAILABLE;
				}

				// Đang hoạt động — "TRUE" (không phân biệt hoa thường) = true
				String isActiveStr = getCellString(formatter, row, 4).toUpperCase();
				boolean isActive = "TRUE".equals(isActiveStr);

				TableEntity table = new TableEntity();
				table.setTableNumber(tableNumber);
				table.setArea(area);
				table.setStatus(status);
				table.setIsActive(isActive || true);

				tables.add(table);
			}
			return tables;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi parse Excel file Table: " + e.getMessage());
		}
	}
}
