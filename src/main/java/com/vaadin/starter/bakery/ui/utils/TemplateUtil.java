package com.vaadin.starter.bakery.ui.utils;

/**
 * Utility class for generating template-related strings and locations.
 * Provides helper methods for URL and navigation path generation.
 */
public class TemplateUtil {

    /**
     * Generates a navigation location by combining a base page path with an optional entity ID.
     * If the entity ID is null or empty, returns just the base page path.
     *
     * @param basePage the base page path (e.g., "storefront", "products")
     * @param entityId the entity ID to append to the path, or null for no entity
     * @return the complete navigation location path
     */
    public static String generateLocation(String basePage, String entityId) {
        return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
    }
}