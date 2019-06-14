package com.brangelov.lunchy.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.brangelov.lunchy.domain.Restaurant} entity. This class is used
 * in {@link com.brangelov.lunchy.web.rest.RestaurantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restaurants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter restaurantLocationId;

    private LongFilter menuId;

    public RestaurantCriteria(){
    }

    public RestaurantCriteria(RestaurantCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.restaurantLocationId = other.restaurantLocationId == null ? null : other.restaurantLocationId.copy();
        this.menuId = other.menuId == null ? null : other.menuId.copy();
    }

    @Override
    public RestaurantCriteria copy() {
        return new RestaurantCriteria(this);
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

    public LongFilter getRestaurantLocationId() {
        return restaurantLocationId;
    }

    public void setRestaurantLocationId(LongFilter restaurantLocationId) {
        this.restaurantLocationId = restaurantLocationId;
    }

    public LongFilter getMenuId() {
        return menuId;
    }

    public void setMenuId(LongFilter menuId) {
        this.menuId = menuId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestaurantCriteria that = (RestaurantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(restaurantLocationId, that.restaurantLocationId) &&
            Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        restaurantLocationId,
        menuId
        );
    }

    @Override
    public String toString() {
        return "RestaurantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (restaurantLocationId != null ? "restaurantLocationId=" + restaurantLocationId + ", " : "") +
                (menuId != null ? "menuId=" + menuId + ", " : "") +
            "}";
    }

}
