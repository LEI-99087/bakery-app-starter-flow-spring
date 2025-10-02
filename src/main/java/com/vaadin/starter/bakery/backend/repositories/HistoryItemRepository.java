package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;

/**
 * Repository interface for HistoryItem entity operations.
 * <p>
 * Provides CRUD operations and query methods for HistoryItem entities
 * through Spring Data JPA. HistoryItems track order state changes and events.
 * </p>
 *
 * @see HistoryItem
 * @see JpaRepository
 */
public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}