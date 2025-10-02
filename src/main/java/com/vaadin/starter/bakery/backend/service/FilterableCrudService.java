package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Extends CrudService with filtering capabilities for entity search and pagination.
 * Provides methods to find and count entities matching optional filter criteria.
 *
 * @param <T> the type of entity this service manages, must extend AbstractEntity
 */
public interface FilterableCrudService<T extends AbstractEntity> extends CrudService<T> {

    /**
     * Finds all entities matching the given filter with pagination support.
     *
     * @param filter an optional filter string to match against entity properties
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of matching entities
     */
    Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

    /**
     * Counts the number of entities matching the given filter.
     *
     * @param filter an optional filter string to match against entity properties
     * @return the count of matching entities
     */
    long countAnyMatching(Optional<String> filter);

}