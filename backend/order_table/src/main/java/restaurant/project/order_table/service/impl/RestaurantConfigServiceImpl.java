package restaurant.project.order_table.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurant.project.order_table.entity.RestaurantConfigEntity;
import restaurant.project.order_table.repository.RestaurantConfigRepository;
import restaurant.project.order_table.service.RestaurantConfigService;

@Service
@RequiredArgsConstructor
public class RestaurantConfigServiceImpl implements RestaurantConfigService {

    private final RestaurantConfigRepository configRepository;

    @Override
    public RestaurantConfigEntity getConfig() {
        return configRepository.findById(1L).orElse(null);
    }

    @Override
    @Transactional
    public RestaurantConfigEntity updateConfig(RestaurantConfigEntity updatedEntity) {
        RestaurantConfigEntity config = configRepository.findById(1L)
                .orElseGet(() -> {
                    updatedEntity.setId(1L);
                    return updatedEntity;
                });

        // Update fields
        config.setName(updatedEntity.getName());
        config.setLogo(updatedEntity.getLogo());
        config.setAddress(updatedEntity.getAddress());
        config.setPhone(updatedEntity.getPhone());
        config.setEmail(updatedEntity.getEmail());
        config.setWebsite(updatedEntity.getWebsite());
        config.setDescription(updatedEntity.getDescription());
        config.setOpeningTime(updatedEntity.getOpeningTime());
        config.setClosingTime(updatedEntity.getClosingTime());
        config.setTaxId(updatedEntity.getTaxId());
        config.setBannerImage(updatedEntity.getBannerImage());
        config.setOperatingHours(updatedEntity.getOperatingHours());

        return configRepository.save(config);
    }
}
