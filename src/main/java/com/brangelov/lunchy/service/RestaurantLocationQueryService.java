package com.brangelov.lunchy.service;

import com.brangelov.lunchy.domain.RestaurantLocation;
import com.brangelov.lunchy.domain.RestaurantLocation_;
import com.brangelov.lunchy.domain.Restaurant_;
import com.brangelov.lunchy.repository.RestaurantLocationRepository;
import com.brangelov.lunchy.service.dto.RestaurantLocationCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link RestaurantLocation} entities in the database.
 * The main input is a {@link RestaurantLocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RestaurantLocation} or a {@link Page} of {@link RestaurantLocation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestaurantLocationQueryService extends QueryService<RestaurantLocation> {

    private final Logger log = LoggerFactory.getLogger(RestaurantLocationQueryService.class);

    private final RestaurantLocationRepository restaurantLocationRepository;

    public RestaurantLocationQueryService(RestaurantLocationRepository restaurantLocationRepository) {
        this.restaurantLocationRepository = restaurantLocationRepository;
    }

    /**
     * Return a {@link List} of {@link RestaurantLocation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurantLocation> findByCriteria(RestaurantLocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RestaurantLocation> specification = createSpecification(criteria);
        return restaurantLocationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RestaurantLocation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RestaurantLocation> findByCriteria(RestaurantLocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RestaurantLocation> specification = createSpecification(criteria);
        return restaurantLocationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RestaurantLocationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RestaurantLocation> specification = createSpecification(criteria);
        return restaurantLocationRepository.count(specification);
    }

    /**
     * Function to convert RestaurantLocationCriteria to a {@link Specification}.
     */
    private Specification<RestaurantLocation> createSpecification(RestaurantLocationCriteria criteria) {
        Specification<RestaurantLocation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RestaurantLocation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RestaurantLocation_.name));
            }
            if (criteria.getLocationString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationString(), RestaurantLocation_.locationString));
            }
            if (criteria.getRestaurantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestaurantId(),
                    root -> root.join(RestaurantLocation_.restaurant, JoinType.LEFT).get(Restaurant_.id)));
            }
        }
        return specification;
    }
}
