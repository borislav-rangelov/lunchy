package com.brangelov.lunchy.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.brangelov.lunchy.domain.RestaurantLocation} entity.
 */
public class RestaurantLocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 2500)
    private String locationString;


    private Long restaurantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RestaurantLocationDTO restaurantLocationDTO = (RestaurantLocationDTO) o;
        if (restaurantLocationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), restaurantLocationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RestaurantLocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationString='" + getLocationString() + "'" +
            ", restaurant=" + getRestaurantId() +
            "}";
    }
}
