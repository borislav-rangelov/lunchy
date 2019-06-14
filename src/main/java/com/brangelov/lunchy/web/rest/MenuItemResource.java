package com.brangelov.lunchy.web.rest;

import com.brangelov.lunchy.domain.MenuItem;
import com.brangelov.lunchy.service.MenuItemQueryService;
import com.brangelov.lunchy.service.MenuItemService;
import com.brangelov.lunchy.service.dto.MenuItemCriteria;
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
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brangelov.lunchy.domain.MenuItem}.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);

    private static final String ENTITY_NAME = "menuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemService menuItemService;

    private final MenuItemQueryService menuItemQueryService;

    public MenuItemResource(MenuItemService menuItemService, MenuItemQueryService menuItemQueryService) {
        this.menuItemService = menuItemService;
        this.menuItemQueryService = menuItemQueryService;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItem.
     *
     * @param menuItem the menuItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItem, or with status {@code 400 (Bad Request)} if the menuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-items} : Updates an existing menuItem.
     *
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-items")
    public ResponseEntity<MenuItem> updateMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}", menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MenuItem result = menuItemService.save(menuItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems(MenuItemCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get MenuItems by criteria: {}", criteria);
        Page<MenuItem> page = menuItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /menu-items/count} : count all the menuItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/menu-items/count")
    public ResponseEntity<Long> countMenuItems(MenuItemCriteria criteria) {
        log.debug("REST request to count MenuItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItem> menuItem = menuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItem);
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
