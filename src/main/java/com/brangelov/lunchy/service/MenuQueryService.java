package com.brangelov.lunchy.service;

import com.brangelov.lunchy.domain.Menu;
import com.brangelov.lunchy.domain.MenuItem_;
import com.brangelov.lunchy.domain.Menu_;
import com.brangelov.lunchy.domain.Restaurant_;
import com.brangelov.lunchy.repository.MenuRepository;
import com.brangelov.lunchy.service.dto.MenuCriteria;
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
 * Service for executing complex queries for {@link Menu} entities in the database.
 * The main input is a {@link MenuCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Menu} or a {@link Page} of {@link Menu} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuQueryService extends QueryService<Menu> {

    private final Logger log = LoggerFactory.getLogger(MenuQueryService.class);

    private final MenuRepository menuRepository;

    public MenuQueryService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Return a {@link List} of {@link Menu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Menu> findByCriteria(MenuCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Menu} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Menu> findByCriteria(MenuCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuRepository.count(specification);
    }

    /**
     * Function to convert MenuCriteria to a {@link Specification}.
     */
    private Specification<Menu> createSpecification(MenuCriteria criteria) {
        Specification<Menu> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Menu_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Menu_.name));
            }
            if (criteria.getValidFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidFrom(), Menu_.validFrom));
            }
            if (criteria.getValidTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidTo(), Menu_.validTo));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuItemId(),
                    root -> root.join(Menu_.menuItems, JoinType.LEFT).get(MenuItem_.id)));
            }
            if (criteria.getRestaurantId() != null) {
                specification = specification.and(buildSpecification(criteria.getRestaurantId(),
                    root -> root.join(Menu_.restaurant, JoinType.LEFT).get(Restaurant_.id)));
            }
        }
        return specification;
    }
}
