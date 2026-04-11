package restaurant.project.order_table.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import restaurant.project.order_table.dto.request.table.TableCreateRequest;
import restaurant.project.order_table.dto.request.table.TableUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.table.TableResponse;
import restaurant.project.order_table.entity.TableEntity;
import restaurant.project.order_table.entity.enums.TableStatus;
import restaurant.project.order_table.mapper.TableMapper;
import restaurant.project.order_table.service.TableService;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;
    private final TableMapper tableMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TableResponse> createTable(@Valid @RequestBody TableCreateRequest request) {
        TableEntity entity = tableMapper.toEntity(request);
        TableEntity created = tableService.createTable(entity);
        return ApiResponse.success(tableMapper.toResponse(created), "Bàn đã được tạo thành công");
    }

    @GetMapping("/{id}")
    public ApiResponse<TableResponse> getTableById(@PathVariable Long id) {
        TableEntity table = tableService.getTableById(id);
        return ApiResponse.success(tableMapper.toResponse(table), "Bàn đã được lấy thành công");
    }

    @GetMapping
    public ApiResponse<List<TableResponse>> getAllTables() {
        List<TableResponse> tables = tableService.getAllTables().stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(tables, "Bàn đã được lấy thành công");
    }

    @PutMapping("/{id}")
    public ApiResponse<TableResponse> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody TableUpdateRequest request) {
        TableEntity entity = tableMapper.toEntity(request);
        TableEntity updated = tableService.updateTable(id, entity);
        return ApiResponse.success(tableMapper.toResponse(updated), "Bàn đã được cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ApiResponse.success(null, "Bàn đã được xóa thành công");
    }

    @GetMapping("/number/{tableNumber}")
    public ApiResponse<TableResponse> getTableByNumber(@PathVariable Integer tableNumber) {
        TableEntity table = tableService.getTableByNumber(tableNumber);
        return ApiResponse.success(tableMapper.toResponse(table), "Bàn đã được lấy thành công");
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<TableResponse>> getTablesByStatus(@PathVariable TableStatus status) {
        List<TableResponse> tables = tableService.getTablesByStatus(status).stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(tables, "Bàn đã được lấy thành công");
    }

    @GetMapping("/area/{area}")
    public ApiResponse<List<TableResponse>> getTablesByArea(@PathVariable String area) {
        List<TableResponse> tables = tableService.getTablesByArea(area).stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(tables, "Bàn đã được lấy thành công");
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<TableResponse> updateTableStatus(
            @PathVariable Long id,
            @RequestParam TableStatus status) {
        TableEntity updated = tableService.updateTableStatus(id, status);
        return ApiResponse.success(tableMapper.toResponse(updated), "Trạng thái bàn đã được cập nhật thành công");
    }

    @GetMapping("/active")
    public ApiResponse<List<TableResponse>> getActiveTables() {
        List<TableResponse> tables = tableService.getActiveTables().stream()
                .map(tableMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(tables, "Bàn đang hoạt động đã được lấy thành công");
    }
}
