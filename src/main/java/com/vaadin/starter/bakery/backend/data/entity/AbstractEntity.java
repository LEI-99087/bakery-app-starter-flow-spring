package com.vaadin.starter.bakery.backend.data.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Abstract base class for all entity classes in the application.
 * <p>
 * Provides common fields and functionality for entity persistence,
 * including primary key generation and optimistic locking version control.
 * </p>
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    /**
     * Gets the unique identifier for this entity.
     *
     * @return the entity's primary key, or null if not persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the version number for optimistic locking.
     *
     * @return the current version number of this entity
     */
    public int getVersion() {
        return version;
    }

    /**
     * Calculates the hash code based on the entity's id and version.
     *
     * @return a hash code value for this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    /**
     * Compares this entity to another object for equality.
     * Two entities are considered equal if they have the same class,
     * same non-null id, and same version.
     *
     * @param o the reference object with which to compare
     * @return true if this entity is the same as the object argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return version == that.version &&
                Objects.equals(id, that.id);
    }
}