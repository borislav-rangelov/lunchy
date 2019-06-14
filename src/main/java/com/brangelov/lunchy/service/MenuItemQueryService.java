package com.brangelov.lunchy.service;

import com.brangelov.lunchy.domain.MenuItem;
import com.brangelov.lunchy.domain.MenuItem_;
import com.brangelov.lunchy.domain.Menu_;
import com.brangelov.lunchy.repository.MenuItemRepository;
import com.brangelov.lunchy.service.dto.MenuItemCriteria;
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
 * Service for executing complex queries for {@link MenuItem} entities in the database.
 * The main input is a {@link MenuItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuItem} or a {@link Page} of {@link MenuItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuItemQueryService extends QueryService<MenuItem> {

    private final Logger log = LoggerFactory.getLogger(MenuItemQueryService.class);

    private final MenuItemRepository menuItemRepository;

    public MenuItemQueryService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Return a {@link List} of {@link MenuItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuItem> findByCriteria(MenuItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MenuItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItem> findByCriteria(MenuItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.count(specification);
    }

    /**
     * Function to convert MenuItemCriteria to a {@link Specification}.
     */
    private Specification<MenuItem> createSpecification(MenuItemCriteria criteria) {
        Specification<MenuItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MenuItem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MenuItem_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MenuItem_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), MenuItem_.price));
            }
            if (criteria.getGrams() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrams(), MenuItem_.grams));
            }
            if (criteria.getMenuId() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuId(),
                    root -> root.join(MenuItem_.menu, JoinType.LEFT).get(Menu_.id)));
            }
        }
        return specification;
    }
}
