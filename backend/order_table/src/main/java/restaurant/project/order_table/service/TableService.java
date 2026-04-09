package restaurant.project.order_table.service;

import java.util.List;

import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.TableStatus;

public interface TableService {

    /**
     * Create a new table
     *
     * @param table table data
     * @return created table
     */
    TableEntity createTable(TableEntity table);

    /**
     * Get table by ID
     *
     * @param id table ID
     * @return table entity
     */
    TableEntity getTableById(Long id);

    /**
     * Get all tables
     *
     * @return list of all tables
     */
    List<TableEntity> getAllTables();

    /**
     * Update table
     *
     * @param id table ID
     * @param table updated table data
     * @return updated table
     */
    TableEntity updateTable(Long id, TableEntity table);

    /**
     * Delete table
     *
     * @param id table ID
     */
    void deleteTable(Long id);

    /**
     * Get table by table number
     *
     * @param tableNumber table number
     * @return table entity
     */
    TableEntity getTableByNumber(Integer tableNumber);

    /**
     * Get tables by status
     *
     * @param status table status
     * @return list of tables
     */
    List<TableEntity> getTablesByStatus(TableStatus status);

    /**
     * Get tables by area
     *
     * @param area area name
     * @return list of tables
     */
    List<TableEntity> getTablesByArea(String area);

    /**
     * Update table status
     *
     * @param id table ID
     * @param status new status
     * @return updated table
     */
    TableEntity updateTableStatus(Long id, TableStatus status);

    /**
     * Get active tables
     *
     * @return list of active tables
     */
    List<TableEntity> getActiveTables();
}
