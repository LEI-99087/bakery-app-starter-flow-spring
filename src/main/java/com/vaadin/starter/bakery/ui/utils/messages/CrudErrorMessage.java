package com.vaadin.starter.bakery.ui.utils.messages;

/**
 * Utility class containing error messages for CRUD operations.
 * Provides standardized error messages for common data operation scenarios.
 */
public final class CrudErrorMessage {

    /** Error message displayed when an entity cannot be found. */
    public static final String ENTITY_NOT_FOUND = "The selected entity was not found.";

    /** Error message displayed when a concurrent update is detected. */
    public static final String CONCURRENT_UPDATE = "Somebody else might have updated the data. Please refresh and try again.";

    /** Error message displayed when an operation is prevented by database references. */
    public static final String OPERATION_PREVENTED_BY_REFERENCES = "The operation can not be executed as there are references to entity in the database.";

    /** Error message displayed when required form fields are missing. */
    public static final String REQUIRED_FIELDS_MISSING = "Please fill out all required fields before proceeding.";

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class that should not be instantiated.
     */
    private CrudErrorMessage() {
    }
}