package com.brangelov.lunchy.service.mapper;

import com.brangelov.lunchy.domain.RestaurantLocation;
import com.brangelov.lunchy.service.dto.RestaurantLocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link RestaurantLocation} and its DTO {@link RestaurantLocationDTO}.
 */
@Mapper(componentModel = "spring", uses = {RestaurantMapper.class})
public interface RestaurantLocationMapper extends EntityMapper<RestaurantLocationDTO, RestaurantLocation> {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    RestaurantLocationDTO toDto(RestaurantLocation restaurantLocation);

    @Mapping(source = "restaurantId", target = "restaurant")
    RestaurantLocation toEntity(RestaurantLocationDTO restaurantLocationDTO);

    default RestaurantLocation fromId(Long id) {
        if (id == null) {
            return null;
        }
        RestaurantLocation restaurantLocation = new RestaurantLocation();
        restaurantLocation.setId(id);
        return restaurantLocation;
    }
}
