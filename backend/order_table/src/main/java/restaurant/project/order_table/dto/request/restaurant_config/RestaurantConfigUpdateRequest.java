package restaurant.project.order_table.dto.request.restaurant_config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantConfigUpdateRequest {
    private String name;
    private String logo;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String description;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String taxId;
    private String bannerImage;
    private String operatingHours;
}
