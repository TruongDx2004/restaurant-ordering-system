package restaurant.project.order_table.service;

import restaurant.project.order_table.entity.RestaurantConfigEntity;

public interface RestaurantConfigService {

    /**
     * Get restaurant configuration.
     * Always returns the configuration with ID 1.
     */
    RestaurantConfigEntity getConfig();

    /**
     * Update restaurant configuration.
     * Always updates the configuration with ID 1.
     */
    RestaurantConfigEntity updateConfig(RestaurantConfigEntity updatedEntity);
}
