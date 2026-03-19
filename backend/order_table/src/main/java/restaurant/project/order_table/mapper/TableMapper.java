package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;
import restaurant.project.order_table.dto.request.table.TableCreateRequest;
import restaurant.project.order_table.dto.request.table.TableUpdateRequest;
import restaurant.project.order_table.dto.response.table.TableResponse;
import restaurant.project.order_table.entity.TableEntity;

@Component
public class TableMapper {

    public TableEntity toEntity(TableCreateRequest request) {
        return TableEntity.builder()
                .tableNumber(request.getTableNumber())
                .area(request.getArea())
                .status(request.getStatus())
                .isActive(request.getIsActive())
                .build();
    }

    public TableEntity toEntity(TableUpdateRequest request) {
        return TableEntity.builder()
                .tableNumber(request.getTableNumber())
                .area(request.getArea())
                .status(request.getStatus())
                .isActive(request.getIsActive())
                .build();
    }

    public TableResponse toResponse(TableEntity entity) {
        return TableResponse.builder()
                .id(entity.getId())
                .tableNumber(entity.getTableNumber())
                .area(entity.getArea())
                .status(entity.getStatus())
                .isActive(entity.getIsActive())
                .build();
    }
}
