package com.brangelov.lunchy.web.rest;

import com.brangelov.lunchy.LunchyApp;
import com.brangelov.lunchy.domain.Menu;
import com.brangelov.lunchy.domain.MenuItem;
import com.brangelov.lunchy.repository.MenuItemRepository;
import com.brangelov.lunchy.service.MenuItemQueryService;
import com.brangelov.lunchy.service.MenuItemService;
import com.brangelov.lunchy.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.brangelov.lunchy.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MenuItemResource} REST controller.
 */
@SpringBootTest(classes = LunchyApp.class)
public class MenuItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 0;
    private static final Integer UPDATED_PRICE = 1;

    private static final Integer DEFAULT_GRAMS = 0;
    private static final Integer UPDATED_GRAMS = 1;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuItemQueryService menuItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MenuItemResource menuItemResource = new MenuItemResource(menuItemService, menuItemQueryService);
        this.restMenuItemMockMvc = MockMvcBuilders.standaloneSetup(menuItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .grams(DEFAULT_GRAMS);
        // Add required entity
        Menu menu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            menu = MenuResourceIT.createEntity(em);
            em.persist(menu);
            em.flush();
        } else {
            menu = TestUtil.findAll(em, Menu.class).get(0);
        }
        menuItem.setMenu(menu);
        return menuItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createUpdatedEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .grams(UPDATED_GRAMS);
        // Add required entity
        Menu menu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            menu = MenuResourceIT.createUpdatedEntity(em);
            em.persist(menu);
            em.flush();
        } else {
            menu = TestUtil.findAll(em, Menu.class).get(0);
        }
        menuItem.setMenu(menu);
        return menuItem;
    }

    @BeforeEach
    public void initTest() {
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenuItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMenuItem.getGrams()).isEqualTo(DEFAULT_GRAMS);
    }

    @Test
    @Transactional
    public void createMenuItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // Create the MenuItem with an existing ID
        menuItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setPrice(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGramsIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setGrams(null);

        // Create the MenuItem, which fails.

        restMenuItemMockMvc.perform(post("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].grams").value(hasItem(DEFAULT_GRAMS)));
    }
    
    @Test
    @Transactional
    public void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.grams").value(DEFAULT_GRAMS));
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name equals to DEFAULT_NAME
        defaultMenuItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name is not null
        defaultMenuItemShouldBeFound("name.specified=true");

        // Get all the menuItemList where name is null
        defaultMenuItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description equals to DEFAULT_DESCRIPTION
        defaultMenuItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMenuItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where description is not null
        defaultMenuItemShouldBeFound("description.specified=true");

        // Get all the menuItemList where description is null
        defaultMenuItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where price equals to DEFAULT_PRICE
        defaultMenuItemShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the menuItemList where price equals to UPDATED_PRICE
        defaultMenuItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMenuItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the menuItemList where price equals to UPDATED_PRICE
        defaultMenuItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where price is not null
        defaultMenuItemShouldBeFound("price.specified=true");

        // Get all the menuItemList where price is null
        defaultMenuItemShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where price greater than or equals to DEFAULT_PRICE
        defaultMenuItemShouldBeFound("price.greaterOrEqualThan=" + DEFAULT_PRICE);

        // Get all the menuItemList where price greater than or equals to UPDATED_PRICE
        defaultMenuItemShouldNotBeFound("price.greaterOrEqualThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where price less than or equals to DEFAULT_PRICE
        defaultMenuItemShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the menuItemList where price less than or equals to UPDATED_PRICE
        defaultMenuItemShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }


    @Test
    @Transactional
    public void getAllMenuItemsByGramsIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where grams equals to DEFAULT_GRAMS
        defaultMenuItemShouldBeFound("grams.equals=" + DEFAULT_GRAMS);

        // Get all the menuItemList where grams equals to UPDATED_GRAMS
        defaultMenuItemShouldNotBeFound("grams.equals=" + UPDATED_GRAMS);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByGramsIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where grams in DEFAULT_GRAMS or UPDATED_GRAMS
        defaultMenuItemShouldBeFound("grams.in=" + DEFAULT_GRAMS + "," + UPDATED_GRAMS);

        // Get all the menuItemList where grams equals to UPDATED_GRAMS
        defaultMenuItemShouldNotBeFound("grams.in=" + UPDATED_GRAMS);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByGramsIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where grams is not null
        defaultMenuItemShouldBeFound("grams.specified=true");

        // Get all the menuItemList where grams is null
        defaultMenuItemShouldNotBeFound("grams.specified=false");
    }

    @Test
    @Transactional
    public void getAllMenuItemsByGramsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where grams greater than or equals to DEFAULT_GRAMS
        defaultMenuItemShouldBeFound("grams.greaterOrEqualThan=" + DEFAULT_GRAMS);

        // Get all the menuItemList where grams greater than or equals to UPDATED_GRAMS
        defaultMenuItemShouldNotBeFound("grams.greaterOrEqualThan=" + UPDATED_GRAMS);
    }

    @Test
    @Transactional
    public void getAllMenuItemsByGramsIsLessThanSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where grams less than or equals to DEFAULT_GRAMS
        defaultMenuItemShouldNotBeFound("grams.lessThan=" + DEFAULT_GRAMS);

        // Get all the menuItemList where grams less than or equals to UPDATED_GRAMS
        defaultMenuItemShouldBeFound("grams.lessThan=" + UPDATED_GRAMS);
    }


    @Test
    @Transactional
    public void getAllMenuItemsByMenuIsEqualToSomething() throws Exception {
        // Get already existing entity
        Menu menu = menuItem.getMenu();
        menuItemRepository.saveAndFlush(menuItem);
        Long menuId = menu.getId();

        // Get all the menuItemList where menu equals to menuId
        defaultMenuItemShouldBeFound("menuId.equals=" + menuId);

        // Get all the menuItemList where menu equals to menuId + 1
        defaultMenuItemShouldNotBeFound("menuId.equals=" + (menuId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuItemShouldBeFound(String filter) throws Exception {
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].grams").value(hasItem(DEFAULT_GRAMS)));

        // Check, that the count call also returns 1
        restMenuItemMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuItemShouldNotBeFound(String filter) throws Exception {
        restMenuItemMockMvc.perform(get("/api/menu-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuItemMockMvc.perform(get("/api/menu-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get("/api/menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .grams(UPDATED_GRAMS);

        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMenuItem)))
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenuItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMenuItem.getGrams()).isEqualTo(UPDATED_GRAMS);
    }

    @Test
    @Transactional
    public void updateNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Create the MenuItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc.perform(put("/api/menu-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuItem)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemService.save(menuItem);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Delete the menuItem
        restMenuItemMockMvc.perform(delete("/api/menu-items/{id}", menuItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1L);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);
        menuItem2.setId(2L);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
        menuItem1.setId(null);
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }
}
