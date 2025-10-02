package com.vaadin.starter.bakery.backend.service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Generic CRUD service interface for managing entities of type T.
 * Provides common create, read, update, and delete operations.
 *
 * @param <T> the type of entity this service manages, must extend AbstractEntity
 */
public interface CrudService<T extends AbstractEntity> {

    /**
     * Gets the JpaRepository instance for the entity type T.
     *
     * @return the JpaRepository instance for entity type T
     */
    JpaRepository<T, Long> getRepository();

    /**
     * Saves the given entity to the database.
     *
     * @param currentUser the user performing the save operation
     * @param entity the entity to be saved
     * @return the saved entity
     */
    default T save(User currentUser, T entity) {
        return getRepository().saveAndFlush(entity);
    }

    /**
     * Deletes the specified entity from the database.
     *
     * @param currentUser the user performing the delete operation
     * @param entity the entity to be deleted
     * @throws EntityNotFoundException if the entity is null
     */
    default void delete(User currentUser, T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        getRepository().delete(entity);
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param currentUser the user performing the delete operation
     * @param id the ID of the entity to be deleted
     * @throws EntityNotFoundException if no entity with the given ID exists
     */
    default void delete(User currentUser, long id) {
        delete(currentUser, load(id));
    }

    /**
     * Returns the total number of entities in the database.
     *
     * @return the total count of entities
     */
    default long count() {
        return getRepository().count();
    }

    /**
     * Loads an entity by its ID.
     *
     * @param id the ID of the entity to load
     * @return the entity with the given ID
     * @throws EntityNotFoundException if no entity with the given ID exists
     */
    default T load(long id) {
        T entity = getRepository().findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    /**
     * Creates a new instance of the entity type T.
     *
     * @param currentUser the user requesting the new entity creation
     * @return a new instance of entity type T
     */
    T createNew(User currentUser);
}