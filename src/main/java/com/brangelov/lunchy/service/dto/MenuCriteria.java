package com.brangelov.lunchy.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.brangelov.lunchy.domain.Menu} entity. This class is used
 * in {@link com.brangelov.lunchy.web.rest.MenuResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MenuCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter validFrom;

    private InstantFilter validTo;

    private LongFilter menuItemId;

    private LongFilter restaurantId;

    public MenuCriteria(){
    }

    public MenuCriteria(MenuCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.validFrom = other.validFrom == null ? null : other.validFrom.copy();
        this.validTo = other.validTo == null ? null : other.validTo.copy();
        this.menuItemId = other.menuItemId == null ? null : other.menuItemId.copy();
        this.restaurantId = other.restaurantId == null ? null : other.restaurantId.copy();
    }

    @Override
    public MenuCriteria copy() {
        return new MenuCriteria(this);
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

    public InstantFilter getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(InstantFilter validFrom) {
        this.validFrom = validFrom;
    }

    public InstantFilter getValidTo() {
        return validTo;
    }

    public void setValidTo(InstantFilter validTo) {
        this.validTo = validTo;
    }

    public LongFilter getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(LongFilter menuItemId) {
        this.menuItemId = menuItemId;
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
        final MenuCriteria that = (MenuCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(validFrom, that.validFrom) &&
            Objects.equals(validTo, that.validTo) &&
            Objects.equals(menuItemId, that.menuItemId) &&
            Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        validFrom,
        validTo,
        menuItemId,
        restaurantId
        );
    }

    @Override
    public String toString() {
        return "MenuCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (validFrom != null ? "validFrom=" + validFrom + ", " : "") +
                (validTo != null ? "validTo=" + validTo + ", " : "") +
                (menuItemId != null ? "menuItemId=" + menuItemId + ", " : "") +
                (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            "}";
    }

}
