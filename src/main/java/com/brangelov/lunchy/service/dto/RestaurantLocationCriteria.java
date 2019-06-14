package com.brangelov.lunchy.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.brangelov.lunchy.domain.RestaurantLocation} entity. This class is used
 * in {@link com.brangelov.lunchy.web.rest.RestaurantLocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restaurant-locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurantLocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter locationString;

    private LongFilter restaurantId;

    public RestaurantLocationCriteria(){
    }

    public RestaurantLocationCriteria(RestaurantLocationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.locationString = other.locationString == null ? null : other.locationString.copy();
        this.restaurantId = other.restaurantId == null ? null : other.restaurantId.copy();
    }

    @Override
    public RestaurantLocationCriteria copy() {
        return new RestaurantLocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLocationString() {
        return locationString;
    }

    public void setLocationString(StringFilter locationString) {
        this.locationString = locationString;
    }

    public LongFilter getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(LongFilter restaurantId) {
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
        final RestaurantLocationCriteria that = (RestaurantLocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(locationString, that.locationString) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        locationString,
        restaurantId
        );
    }

    @Override
    public String toString() {
        return "RestaurantLocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (locationString != null ? "locationString=" + locationString + ", " : "") +
                (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            "}";
    }

}
