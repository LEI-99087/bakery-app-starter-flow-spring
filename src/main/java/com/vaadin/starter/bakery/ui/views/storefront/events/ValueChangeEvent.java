package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemsEditor;

/**
 * Event class representing a value change in order items.
 * This event is fired when any value within the order items editor changes,
 * indicating that there are unsaved modifications.
 */
public class ValueChangeEvent extends ComponentEvent<OrderItemsEditor> {

    /**
     * Constructs a new ValueChangeEvent.
     *
     * @param component the OrderItemsEditor that triggered the value change
     */
    public ValueChangeEvent(OrderItemsEditor component) {
        super(component, false);
    }
}