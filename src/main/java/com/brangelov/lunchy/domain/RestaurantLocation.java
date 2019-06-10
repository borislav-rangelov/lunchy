package com.brangelov.lunchy.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RestaurantLocation.
 */
@Entity
@Table(name = "restaurant_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RestaurantLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 2500)
    @Column(name = "location_string", length = 2500)
    private String locationString;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("restaurantLocations")
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RestaurantLocation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationString() {
        return locationString;
    }

    public RestaurantLocation locationString(String locationString) {
        this.locationString = locationString;
        return this;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public RestaurantLocation restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantLocation)) {
            return false;
        }
        return id != null && id.equals(((RestaurantLocation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RestaurantLocation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationString='" + getLocationString() + "'" +
            "}";
    }
}
