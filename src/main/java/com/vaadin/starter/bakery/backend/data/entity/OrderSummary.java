package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.OrderState;

/**
 * Interface defining a summary view of an order with essential information.
 * <p>
 * This interface provides access to key order details without exposing
 * the complete order entity, useful for listings and summary views.
 * </p>
 */
public interface OrderSummary {

    /**
     * Gets the unique identifier of the order.
     *
     * @return the order ID
     */
    Long getId();

    /**
     * Gets the current state of the order.
     *
     * @return the order state
     */
    OrderState getState();

    /**
     * Gets the customer who placed the order.
     *
     * @return the customer
     */
    Customer getCustomer();

    /**
     * Gets the list of items in the order.
     *
     * @return the list of order items
     */
    List<OrderItem> getItems();

    /**
     * Gets the due date for order fulfillment.
     *
     * @return the due date
     */
    LocalDate getDueDate();

    /**
     * Gets the due time for order fulfillment.
     *
     * @return the due time
     */
    LocalTime getDueTime();

    /**
     * Gets the pickup location for the order.
     *
     * @return the pickup location
     */
    PickupLocation getPickupLocation();

    /**
     * Calculates the total price of the order.
     *
     * @return the total price as the sum of all item prices
     */
    Integer getTotalPrice();
}