package com.vaadin.starter.bakery.backend.data;

import java.util.Locale;

import com.vaadin.flow.shared.util.SharedUtil;

/**
 * Represents the possible states of an order in the bakery system.
 * <p>
 * Tracks the lifecycle of an order from creation to completion or cancellation.
 * </p>
 */
public enum OrderState {
    /** New order that has been placed but not yet processed */
    NEW,
    /** Order has been confirmed and is being prepared */
    CONFIRMED,
    /** Order is ready for pickup/delivery */
    READY,
    /** Order has been successfully delivered to the customer */
    DELIVERED,
    /** Order has encountered a problem during processing */
    PROBLEM,
    /** Order has been cancelled */
    CANCELLED;

    /**
     * Gets a version of the enum identifier in a human friendly format.
     * <p>
     * Converts the enum name to lowercase and capitalizes the first letter
     * for display purposes.
     * </p>
     *
     * @return a human friendly version of the identifier
     */
    public String getDisplayName() {
        return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
    }
}