package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;

/**
 * Repository interface for PickupLocation entity operations.
 * <p>
 * Provides CRUD operations and custom query methods for PickupLocation entities
 * through Spring Data JPA. Includes methods for filtering and pagination.
 * </p>
 *
 * @see PickupLocation
 * @see JpaRepository
 */
public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {

    /**
     * Finds pickup locations by name containing the search query (case-insensitive).
     *
     * @param nameFilter the string to search for in pickup location names
     * @param pageable pagination information
     * @return page of pickup locations matching the name filter
     */
    Page<PickupLocation> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);

    /**
     * Counts pickup locations by name containing the search query (case-insensitive).
     *
     * @param nameFilter the string to search for in pickup location names
     * @return count of pickup locations matching the name filter
     */
    int countByNameLikeIgnoreCase(String nameFilter);
}