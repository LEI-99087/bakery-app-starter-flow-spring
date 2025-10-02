package com.vaadin.starter.bakery.backend.data;

/**
 * Defines the roles available in the bakery application.
 * <p>
 * Roles determine user permissions and access levels within the system.
 * </p>
 */
public class Role {

    /** Role for barista users who can manage orders */
    public static final String BARISTA = "barista";

    /** Role for baker users who can prepare orders */
    public static final String BAKER = "baker";

    /**
     * Administrative role with full system access.
     * This role implicitly allows access to all views.
     */
    public static final String ADMIN = "admin";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Role() {
        // Static methods and fields only
    }

    /**
     * Returns all available roles in the system.
     *
     * @return an array containing all role identifiers
     */
    public static String[] getAllRoles() {
        return new String[] { BARISTA, BAKER, ADMIN };
    }

}