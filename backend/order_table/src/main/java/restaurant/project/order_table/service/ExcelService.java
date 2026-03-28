package restaurant.project.order_table.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;

public interface ExcelService {

    /**
     * Export data to Excel file based on entity type
     *
     * @param entityType type of entity (category, dish, user, table)
     * @return ByteArrayInputStream containing Excel file data
     */
    ByteArrayInputStream exportData(String entityType);

    /**
     * Import data from Excel file and save to database
     *
     * @param entityType type of entity
     * @param file Excel file from client
     */
    void importData(String entityType, MultipartFile file);
}
