package restaurant.project.order_table.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restaurant.project.order_table.dto.request.restaurant_config.RestaurantConfigUpdateRequest;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.dto.response.restaurant_config.RestaurantConfigResponse;
import restaurant.project.order_table.entity.RestaurantConfigEntity;
import restaurant.project.order_table.mapper.RestaurantConfigMapper;
import restaurant.project.order_table.service.RestaurantConfigService;

@RestController
@RequestMapping("/api/restaurant-config")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestaurantConfigController {

    private final RestaurantConfigService configService;
    private final RestaurantConfigMapper configMapper;

    /**
     * Get restaurant configuration.
     * @return ApiResponse containing RestaurantConfigResponse
     */
    @GetMapping
    public ApiResponse<RestaurantConfigResponse> getConfig() {
        RestaurantConfigEntity config = configService.getConfig();
        if (config == null) {
            return ApiResponse.success(null, "Restaurant configuration not found");
        }
        return ApiResponse.success(configMapper.toResponse(config), "Restaurant configuration retrieved successfully");
    }

    /**
     * Update restaurant configuration.
     * @param request the configuration update request
     * @return ApiResponse containing the updated RestaurantConfigResponse
     */
    @PutMapping
    public ApiResponse<RestaurantConfigResponse> updateConfig(@RequestBody RestaurantConfigUpdateRequest request) {
        RestaurantConfigEntity entity = configMapper.toEntity(request);
        RestaurantConfigEntity updated = configService.updateConfig(entity);
        return ApiResponse.success(configMapper.toResponse(updated), "Restaurant configuration updated successfully");
    }
}
