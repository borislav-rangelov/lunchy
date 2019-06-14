package com.brangelov.lunchy.service;

import com.brangelov.lunchy.domain.RestaurantLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RestaurantLocation}.
 */
public interface RestaurantLocationService {

    /**
     * Save a restaurantLocation.
     *
     * @param restaurantLocation the entity to save.
     * @return the persisted entity.
     */
    RestaurantLocation save(RestaurantLocation restaurantLocation);

    /**
     * Get all the restaurantLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantLocation> findAll(Pageable pageable);


    /**
     * Get the "id" restaurantLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantLocation> findOne(Long id);

    /**
     * Delete the "id" restaurantLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
