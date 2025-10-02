package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event class representing a price change in an order item.
 * This event is fired when the price of an order item changes due to quantity
 * or product selection modifications.
 */
public class PriceChangeEvent extends ComponentEvent<OrderItemEditor> {

    private final int oldValue;

    private final int newValue;

    /**
     * Constructs a new PriceChangeEvent.
     *
     * @param component the OrderItemEditor that triggered the price change
     * @param oldValue the previous price value in cents
     * @param newValue the new price value in cents
     */
    public PriceChangeEvent(OrderItemEditor component, int oldValue, int newValue) {
        super(component, false);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the previous price value before the change.
     *
     * @return the old price value in cents
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * Gets the new price value after the change.
     *
     * @return the new price value in cents
     */
    public int getNewValue() {
        return newValue;
    }

}