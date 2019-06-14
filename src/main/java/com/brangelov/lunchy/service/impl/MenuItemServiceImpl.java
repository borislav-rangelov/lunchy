package com.brangelov.lunchy.service.impl;

import com.brangelov.lunchy.domain.MenuItem;
import com.brangelov.lunchy.repository.MenuItemRepository;
import com.brangelov.lunchy.service.MenuItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MenuItem}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Save a menuItem.
     *
     * @param menuItem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MenuItem save(MenuItem menuItem) {
        log.debug("Request to save MenuItem : {}", menuItem);
        return menuItemRepository.save(menuItem);
    }

    /**
     * Get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MenuItem> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll(pageable);
    }


    /**
     * Get one menuItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItem> findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id);
    }

    /**
     * Delete the menuItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
