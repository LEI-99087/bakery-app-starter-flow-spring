package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemsEditor;

/**
 * Event class representing a total price change for all order items.
 * This event is fired when the combined total price of all order items changes.
 */
public class TotalPriceChangeEvent extends ComponentEvent<OrderItemsEditor> {

    private final Integer totalPrice;

    /**
     * Constructs a new TotalPriceChangeEvent.
     *
     * @param component the OrderItemsEditor that triggered the total price change
     * @param totalPrice the new total price value in cents
     */
    public TotalPriceChangeEvent(OrderItemsEditor component, Integer totalPrice) {
        super(component, false);
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the total price of all order items.
     *
     * @return the total price in cents
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }

}