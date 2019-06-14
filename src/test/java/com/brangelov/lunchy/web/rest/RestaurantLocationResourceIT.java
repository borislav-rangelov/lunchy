package com.brangelov.lunchy.web.rest;

import com.brangelov.lunchy.LunchyApp;
import com.brangelov.lunchy.domain.Restaurant;
import com.brangelov.lunchy.domain.RestaurantLocation;
import com.brangelov.lunchy.repository.RestaurantLocationRepository;
import com.brangelov.lunchy.service.RestaurantLocationQueryService;
import com.brangelov.lunchy.service.RestaurantLocationService;
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
 * Integration tests for the {@Link RestaurantLocationResource} REST controller.
 */
@SpringBootTest(classes = LunchyApp.class)
public class RestaurantLocationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_STRING = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_STRING = "BBBBBBBBBB";

    @Autowired
    private RestaurantLocationRepository restaurantLocationRepository;

    @Autowired
    private RestaurantLocationService restaurantLocationService;

    @Autowired
    private RestaurantLocationQueryService restaurantLocationQueryService;

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

    private MockMvc restRestaurantLocationMockMvc;

    private RestaurantLocation restaurantLocation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestaurantLocationResource restaurantLocationResource = new RestaurantLocationResource(restaurantLocationService, restaurantLocationQueryService);
        this.restRestaurantLocationMockMvc = MockMvcBuilders.standaloneSetup(restaurantLocationResource)
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
    public static RestaurantLocation createEntity(EntityManager em) {
        RestaurantLocation restaurantLocation = new RestaurantLocation()
            .name(DEFAULT_NAME)
            .locationString(DEFAULT_LOCATION_STRING);
        // Add required entity
        Restaurant restaurant;
        if (TestUtil.findAll(em, Restaurant.class).isEmpty()) {
            restaurant = RestaurantResourceIT.createEntity(em);
            em.persist(restaurant);
            em.flush();
        } else {
            restaurant = TestUtil.findAll(em, Restaurant.class).get(0);
        }
        restaurantLocation.setRestaurant(restaurant);
        return restaurantLocation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantLocation createUpdatedEntity(EntityManager em) {
        RestaurantLocation restaurantLocation = new RestaurantLocation()
            .name(UPDATED_NAME)
            .locationString(UPDATED_LOCATION_STRING);
        // Add required entity
        Restaurant restaurant;
        if (TestUtil.findAll(em, Restaurant.class).isEmpty()) {
            restaurant = RestaurantResourceIT.createUpdatedEntity(em);
            em.persist(restaurant);
            em.flush();
        } else {
            restaurant = TestUtil.findAll(em, Restaurant.class).get(0);
        }
        restaurantLocation.setRestaurant(restaurant);
        return restaurantLocation;
    }

    @BeforeEach
    public void initTest() {
        restaurantLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurantLocation() throws Exception {
        int databaseSizeBeforeCreate = restaurantLocationRepository.findAll().size();

        // Create the RestaurantLocation
        restRestaurantLocationMockMvc.perform(post("/api/restaurant-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantLocation)))
            .andExpect(status().isCreated());

        // Validate the RestaurantLocation in the database
        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantLocation testRestaurantLocation = restaurantLocationList.get(restaurantLocationList.size() - 1);
        assertThat(testRestaurantLocation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurantLocation.getLocationString()).isEqualTo(DEFAULT_LOCATION_STRING);
    }

    @Test
    @Transactional
    public void createRestaurantLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restaurantLocationRepository.findAll().size();

        // Create the RestaurantLocation with an existing ID
        restaurantLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantLocationMockMvc.perform(post("/api/restaurant-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantLocation)))
            .andExpect(status().isBadRequest());

        // Validate the RestaurantLocation in the database
        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantLocationRepository.findAll().size();
        // set the field null
        restaurantLocation.setName(null);

        // Create the RestaurantLocation, which fails.

        restRestaurantLocationMockMvc.perform(post("/api/restaurant-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantLocation)))
            .andExpect(status().isBadRequest());

        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRestaurantLocations() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].locationString").value(hasItem(DEFAULT_LOCATION_STRING.toString())));
    }
    
    @Test
    @Transactional
    public void getRestaurantLocation() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get the restaurantLocation
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations/{id}", restaurantLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantLocation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.locationString").value(DEFAULT_LOCATION_STRING.toString()));
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where name equals to DEFAULT_NAME
        defaultRestaurantLocationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the restaurantLocationList where name equals to UPDATED_NAME
        defaultRestaurantLocationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRestaurantLocationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the restaurantLocationList where name equals to UPDATED_NAME
        defaultRestaurantLocationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where name is not null
        defaultRestaurantLocationShouldBeFound("name.specified=true");

        // Get all the restaurantLocationList where name is null
        defaultRestaurantLocationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByLocationStringIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where locationString equals to DEFAULT_LOCATION_STRING
        defaultRestaurantLocationShouldBeFound("locationString.equals=" + DEFAULT_LOCATION_STRING);

        // Get all the restaurantLocationList where locationString equals to UPDATED_LOCATION_STRING
        defaultRestaurantLocationShouldNotBeFound("locationString.equals=" + UPDATED_LOCATION_STRING);
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByLocationStringIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where locationString in DEFAULT_LOCATION_STRING or UPDATED_LOCATION_STRING
        defaultRestaurantLocationShouldBeFound("locationString.in=" + DEFAULT_LOCATION_STRING + "," + UPDATED_LOCATION_STRING);

        // Get all the restaurantLocationList where locationString equals to UPDATED_LOCATION_STRING
        defaultRestaurantLocationShouldNotBeFound("locationString.in=" + UPDATED_LOCATION_STRING);
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByLocationStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantLocationRepository.saveAndFlush(restaurantLocation);

        // Get all the restaurantLocationList where locationString is not null
        defaultRestaurantLocationShouldBeFound("locationString.specified=true");

        // Get all the restaurantLocationList where locationString is null
        defaultRestaurantLocationShouldNotBeFound("locationString.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestaurantLocationsByRestaurantIsEqualToSomething() throws Exception {
        // Get already existing entity
        Restaurant restaurant = restaurantLocation.getRestaurant();
        restaurantLocationRepository.saveAndFlush(restaurantLocation);
        Long restaurantId = restaurant.getId();

        // Get all the restaurantLocationList where restaurant equals to restaurantId
        defaultRestaurantLocationShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the restaurantLocationList where restaurant equals to restaurantId + 1
        defaultRestaurantLocationShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRestaurantLocationShouldBeFound(String filter) throws Exception {
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].locationString").value(hasItem(DEFAULT_LOCATION_STRING)));

        // Check, that the count call also returns 1
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRestaurantLocationShouldNotBeFound(String filter) throws Exception {
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRestaurantLocation() throws Exception {
        // Get the restaurantLocation
        restRestaurantLocationMockMvc.perform(get("/api/restaurant-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurantLocation() throws Exception {
        // Initialize the database
        restaurantLocationService.save(restaurantLocation);

        int databaseSizeBeforeUpdate = restaurantLocationRepository.findAll().size();

        // Update the restaurantLocation
        RestaurantLocation updatedRestaurantLocation = restaurantLocationRepository.findById(restaurantLocation.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantLocation are not directly saved in db
        em.detach(updatedRestaurantLocation);
        updatedRestaurantLocation
            .name(UPDATED_NAME)
            .locationString(UPDATED_LOCATION_STRING);

        restRestaurantLocationMockMvc.perform(put("/api/restaurant-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestaurantLocation)))
            .andExpect(status().isOk());

        // Validate the RestaurantLocation in the database
        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeUpdate);
        RestaurantLocation testRestaurantLocation = restaurantLocationList.get(restaurantLocationList.size() - 1);
        assertThat(testRestaurantLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurantLocation.getLocationString()).isEqualTo(UPDATED_LOCATION_STRING);
    }

    @Test
    @Transactional
    public void updateNonExistingRestaurantLocation() throws Exception {
        int databaseSizeBeforeUpdate = restaurantLocationRepository.findAll().size();

        // Create the RestaurantLocation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantLocationMockMvc.perform(put("/api/restaurant-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurantLocation)))
            .andExpect(status().isBadRequest());

        // Validate the RestaurantLocation in the database
        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRestaurantLocation() throws Exception {
        // Initialize the database
        restaurantLocationService.save(restaurantLocation);

        int databaseSizeBeforeDelete = restaurantLocationRepository.findAll().size();

        // Delete the restaurantLocation
        restRestaurantLocationMockMvc.perform(delete("/api/restaurant-locations/{id}", restaurantLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<RestaurantLocation> restaurantLocationList = restaurantLocationRepository.findAll();
        assertThat(restaurantLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantLocation.class);
        RestaurantLocation restaurantLocation1 = new RestaurantLocation();
        restaurantLocation1.setId(1L);
        RestaurantLocation restaurantLocation2 = new RestaurantLocation();
        restaurantLocation2.setId(restaurantLocation1.getId());
        assertThat(restaurantLocation1).isEqualTo(restaurantLocation2);
        restaurantLocation2.setId(2L);
        assertThat(restaurantLocation1).isNotEqualTo(restaurantLocation2);
        restaurantLocation1.setId(null);
        assertThat(restaurantLocation1).isNotEqualTo(restaurantLocation2);
    }
}
