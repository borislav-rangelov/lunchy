package com.brangelov.lunchy.service.impl;

import com.brangelov.lunchy.domain.RestaurantLocation;
import com.brangelov.lunchy.repository.RestaurantLocationRepository;
import com.brangelov.lunchy.service.RestaurantLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RestaurantLocation}.
 */
@Service
@Transactional
public class RestaurantLocationServiceImpl implements RestaurantLocationService {

    private final Logger log = LoggerFactory.getLogger(RestaurantLocationServiceImpl.class);

    private final RestaurantLocationRepository restaurantLocationRepository;

    public RestaurantLocationServiceImpl(RestaurantLocationRepository restaurantLocationRepository) {
        this.restaurantLocationRepository = restaurantLocationRepository;
    }

    /**
     * Save a restaurantLocation.
     *
     * @param restaurantLocation the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RestaurantLocation save(RestaurantLocation restaurantLocation) {
        log.debug("Request to save RestaurantLocation : {}", restaurantLocation);
        return restaurantLocationRepository.save(restaurantLocation);
    }

    /**
     * Get all the restaurantLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantLocation> findAll(Pageable pageable) {
        log.debug("Request to get all RestaurantLocations");
        return restaurantLocationRepository.findAll(pageable);
    }


    /**
     * Get one restaurantLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantLocation> findOne(Long id) {
        log.debug("Request to get RestaurantLocation : {}", id);
        return restaurantLocationRepository.findById(id);
    }

    /**
     * Delete the restaurantLocation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantLocation : {}", id);
        restaurantLocationRepository.deleteById(id);
    }
}
