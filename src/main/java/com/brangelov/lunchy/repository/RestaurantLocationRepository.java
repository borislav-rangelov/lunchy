package com.brangelov.lunchy.repository;

import com.brangelov.lunchy.domain.RestaurantLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RestaurantLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation, Long> {

}
