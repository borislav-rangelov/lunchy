package com.brangelov.lunchy.web.rest;

import com.brangelov.lunchy.domain.RestaurantLocation;
import com.brangelov.lunchy.repository.RestaurantLocationRepository;
import com.brangelov.lunchy.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brangelov.lunchy.domain.RestaurantLocation}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantLocationResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantLocationResource.class);

    private static final String ENTITY_NAME = "restaurantLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantLocationRepository restaurantLocationRepository;

    public RestaurantLocationResource(RestaurantLocationRepository restaurantLocationRepository) {
        this.restaurantLocationRepository = restaurantLocationRepository;
    }

    /**
     * {@code POST  /restaurant-locations} : Create a new restaurantLocation.
     *
     * @param restaurantLocation the restaurantLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantLocation, or with status {@code 400 (Bad Request)} if the restaurantLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurant-locations")
    public ResponseEntity<RestaurantLocation> createRestaurantLocation(@Valid @RequestBody RestaurantLocation restaurantLocation) throws URISyntaxException {
        log.debug("REST request to save RestaurantLocation : {}", restaurantLocation);
        if (restaurantLocation.getId() != null) {
            throw new BadRequestAlertException("A new restaurantLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantLocation result = restaurantLocationRepository.save(restaurantLocation);
        return ResponseEntity.created(new URI("/api/restaurant-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurant-locations} : Updates an existing restaurantLocation.
     *
     * @param restaurantLocation the restaurantLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantLocation,
     * or with status {@code 400 (Bad Request)} if the restaurantLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurant-locations")
    public ResponseEntity<RestaurantLocation> updateRestaurantLocation(@Valid @RequestBody RestaurantLocation restaurantLocation) throws URISyntaxException {
        log.debug("REST request to update RestaurantLocation : {}", restaurantLocation);
        if (restaurantLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RestaurantLocation result = restaurantLocationRepository.save(restaurantLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantLocation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /restaurant-locations} : get all the restaurantLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantLocations in body.
     */
    @GetMapping("/restaurant-locations")
    public ResponseEntity<List<RestaurantLocation>> getAllRestaurantLocations(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of RestaurantLocations");
        Page<RestaurantLocation> page = restaurantLocationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurant-locations/:id} : get the "id" restaurantLocation.
     *
     * @param id the id of the restaurantLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurant-locations/{id}")
    public ResponseEntity<RestaurantLocation> getRestaurantLocation(@PathVariable Long id) {
        log.debug("REST request to get RestaurantLocation : {}", id);
        Optional<RestaurantLocation> restaurantLocation = restaurantLocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(restaurantLocation);
    }

    /**
     * {@code DELETE  /restaurant-locations/:id} : delete the "id" restaurantLocation.
     *
     * @param id the id of the restaurantLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurant-locations/{id}")
    public ResponseEntity<Void> deleteRestaurantLocation(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantLocation : {}", id);
        restaurantLocationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
