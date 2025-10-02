package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.PickupLocationRepository;

/**
 * Service class for managing PickupLocation entities.
 * Provides business logic for pickup location operations including filtering,
 * counting, and retrieving default locations.
 */
@Service
public class PickupLocationService implements FilterableCrudService<PickupLocation>{

    private final PickupLocationRepository pickupLocationRepository;

    /**
     * Constructs a PickupLocationService with the specified PickupLocationRepository.
     *
     * @param pickupLocationRepository the repository used for pickup location data access
     */
    @Autowired
    public PickupLocationService(PickupLocationRepository pickupLocationRepository) {
        this.pickupLocationRepository = pickupLocationRepository;
    }

    /**
     * Finds pickup locations matching the given filter with pagination support.
     *
     * @param filter an optional filter string to match against pickup location names
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of matching pickup locations
     */
    public Page<PickupLocation> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return pickupLocationRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
        } else {
            return pickupLocationRepository.findAll(pageable);
        }
    }

    /**
     * Counts the number of pickup locations matching the given filter.
     *
     * @param filter an optional filter string to match against pickup location names
     * @return the count of matching pickup locations
     */
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return pickupLocationRepository.countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return pickupLocationRepository.count();
        }
    }

    /**
     * Gets the default pickup location.
     * Returns the first pickup location from the unfiltered results.
     *
     * @return the default pickup location
     */
    public PickupLocation getDefault() {
        return findAnyMatching(Optional.empty(), PageRequest.of(0, 1)).iterator().next();
    }

    /**
     * Gets the PickupLocationRepository instance.
     *
     * @return the PickupLocationRepository instance
     */
    @Override
    public JpaRepository<PickupLocation, Long> getRepository() {
        return pickupLocationRepository;
    }

    /**
     * Creates a new pickup location instance.
     *
     * @param currentUser the user creating the pickup location
     * @return a new PickupLocation instance
     */
    @Override
    public PickupLocation createNew(User currentUser) {
        return new PickupLocation();
    }
}