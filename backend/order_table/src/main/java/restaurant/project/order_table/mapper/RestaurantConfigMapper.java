package restaurant.project.order_table.mapper;

import org.springframework.stereotype.Component;
import restaurant.project.order_table.dto.request.restaurant_config.RestaurantConfigUpdateRequest;
import restaurant.project.order_table.dto.response.restaurant_config.RestaurantConfigResponse;
import restaurant.project.order_table.entity.RestaurantConfigEntity;

@Component
public class RestaurantConfigMapper {

    public RestaurantConfigEntity toEntity(RestaurantConfigUpdateRequest request) {
        return RestaurantConfigEntity.builder()
                .name(request.getName())
                .logo(request.getLogo())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .website(request.getWebsite())
                .description(request.getDescription())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .taxId(request.getTaxId())
                .bannerImage(request.getBannerImage())
                .bannerImage2(request.getBannerImage2())
                .bannerImage3(request.getBannerImage3())
                .bannerImage4(request.getBannerImage4())
                .operatingHours(request.getOperatingHours())
                .build();
    }

    public RestaurantConfigResponse toResponse(RestaurantConfigEntity entity) {
        if (entity == null) return null;
        
        return RestaurantConfigResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .logo(entity.getLogo())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .website(entity.getWebsite())
                .description(entity.getDescription())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .taxId(entity.getTaxId())
                .bannerImage(entity.getBannerImage())
                .bannerImage2(entity.getBannerImage2())
                .bannerImage3(entity.getBannerImage3())
                .bannerImage4(entity.getBannerImage4())
                .operatingHours(entity.getOperatingHours())
                .build();
    }
}
