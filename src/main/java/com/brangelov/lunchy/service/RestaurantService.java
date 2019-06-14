package com.brangelov.lunchy.service;

import com.brangelov.lunchy.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Restaurant}.
 */
public interface RestaurantService {

    /**
     * Save a restaurant.
     *
     * @param restaurant the entity to save.
     * @return the persisted entity.
     */
    Restaurant save(Restaurant restaurant);

    /**
     * Get all the restaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Restaurant> findAll(Pageable pageable);


    /**
     * Get the "id" restaurant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Restaurant> findOne(Long id);

    /**
     * Delete the "id" restaurant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
