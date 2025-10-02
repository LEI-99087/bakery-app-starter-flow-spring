package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.Customer;

/**
 * Repository interface for Customer entity operations.
 * <p>
 * Provides CRUD operations and query methods for Customer entities
 * through Spring Data JPA.
 * </p>
 *
 * @see Customer
 * @see JpaRepository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}