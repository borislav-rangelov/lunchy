package com.brangelov.lunchy.web.rest;

import com.brangelov.lunchy.domain.Menu;
import com.brangelov.lunchy.service.MenuQueryService;
import com.brangelov.lunchy.service.MenuService;
import com.brangelov.lunchy.service.dto.MenuCriteria;
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
 * REST controller for managing {@link com.brangelov.lunchy.domain.Menu}.
 */
@RestController
@RequestMapping("/api")
public class MenuResource {

    private final Logger log = LoggerFactory.getLogger(MenuResource.class);

    private static final String ENTITY_NAME = "menu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuService menuService;

    private final MenuQueryService menuQueryService;

    public MenuResource(MenuService menuService, MenuQueryService menuQueryService) {
        this.menuService = menuService;
        this.menuQueryService = menuQueryService;
    }

    /**
     * {@code POST  /menus} : Create a new menu.
     *
     * @param menu the menu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menu, or with status {@code 400 (Bad Request)} if the menu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menus")
    public ResponseEntity<Menu> createMenu(@Valid @RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to save Menu : {}", menu);
        if (menu.getId() != null) {
            throw new BadRequestAlertException("A new menu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Menu result = menuService.save(menu);
        return ResponseEntity.created(new URI("/api/menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menus} : Updates an existing menu.
     *
     * @param menu the menu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menu,
     * or with status {@code 400 (Bad Request)} if the menu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menus")
    public ResponseEntity<Menu> updateMenu(@Valid @RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to update Menu : {}", menu);
        if (menu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Menu result = menuService.save(menu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menu.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /menus} : get all the menus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menus in body.
     */
    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getAllMenus(MenuCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Menus by criteria: {}", criteria);
        Page<Menu> page = menuQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /menus/count} : count all the menus.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/menus/count")
    public ResponseEntity<Long> countMenus(MenuCriteria criteria) {
        log.debug("REST request to count Menus by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menus/:id} : get the "id" menu.
     *
     * @param id the id of the menu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menus/{id}")
    public ResponseEntity<Menu> getMenu(@PathVariable Long id) {
        log.debug("REST request to get Menu : {}", id);
        Optional<Menu> menu = menuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menu);
    }

    /**
     * {@code DELETE  /menus/:id} : delete the "id" menu.
     *
     * @param id the id of the menu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        log.debug("REST request to delete Menu : {}", id);
        menuService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
