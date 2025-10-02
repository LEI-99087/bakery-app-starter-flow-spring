package com.vaadin.starter.bakery.backend.data.entity.util;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

/**
 * Utility class providing common operations for entity classes.
 * <p>
 * This class contains static helper methods for working with entities
 * in the application.
 * </p>
 */
public final class EntityUtil {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private EntityUtil() {
    }

    /**
     * Returns the name of the entity class.
     * <p>
     * All main entities have simple one word names, so this is sufficient.
     * Metadata could be added to the class if necessary.
     * </p>
     *
     * @param type the entity class to get the name for
     * @return the simple class name of the entity
     */
    public static final String getName(Class<? extends AbstractEntity> type) {
        // All main entities have simple one word names, so this is sufficient. Metadata
        // could be added to the class if necessary.
        return type.getSimpleName();
    }
}