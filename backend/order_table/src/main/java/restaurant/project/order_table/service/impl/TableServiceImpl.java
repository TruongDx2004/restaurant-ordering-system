package restaurant.project.order_table.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.TableStatus;
import restaurant.project.order_table.exception.BadRequestException;
import restaurant.project.order_table.repository.TableRepository;
import restaurant.project.order_table.service.TableService;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

	private final TableRepository tableRepository;

	@Override
	public TableEntity createTable(TableEntity table) {
		// Check if table number already exists
		tableRepository.findByTableNumber(table.getTableNumber())
				.ifPresent(t -> {
					throw new BadRequestException("Số bàn đã tồn tại: " + table.getTableNumber());
				});

		return tableRepository.save(table);
	}

	@Override
	public TableEntity getTableById(Long id) {
		return tableRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Bàn không tồn tại với id: " + id));
	}

	@Override
	public List<TableEntity> getAllTables() {
		return tableRepository.findAll();
	}

	@Override
	public TableEntity updateTable(Long id, TableEntity table) {
		TableEntity existingTable = getTableById(id);

		if (!existingTable.getTableNumber().equals(table.getTableNumber())) {
			tableRepository.findByTableNumber(table.getTableNumber())
					.ifPresent(t -> {
						throw new BadRequestException("Số bàn đã tồn tại: " + table.getTableNumber());
					});
		}

		existingTable.setTableNumber(table.getTableNumber());
		existingTable.setArea(table.getArea());
		existingTable.setStatus(table.getStatus());
		existingTable.setIsActive(table.getIsActive() || true);

		return tableRepository.save(existingTable);
	}

	@Override
	public void deleteTable(Long id) {
		TableEntity table = getTableById(id);
		tableRepository.delete(table);
	}

	@Override
	public TableEntity getTableByNumber(Integer tableNumber) {
		return tableRepository.findByTableNumber(tableNumber)
				.orElseThrow(() -> new BadRequestException("Bàn không tồn tại với số bàn: " + tableNumber));
	}

	@Override
	public List<TableEntity> getTablesByStatus(TableStatus status) {
		return tableRepository.findByStatus(status);
	}

	@Override
	public List<TableEntity> getTablesByArea(String area) {
		return tableRepository.findByArea(area);
	}

	@Override
	public TableEntity updateTableStatus(Long id, TableStatus status) {
		TableEntity table = getTableById(id);
		table.setStatus(status);
		return tableRepository.save(table);
	}

	@Override
	public List<TableEntity> getActiveTables() {
		return tableRepository.findByIsActive(true);
	}
}
