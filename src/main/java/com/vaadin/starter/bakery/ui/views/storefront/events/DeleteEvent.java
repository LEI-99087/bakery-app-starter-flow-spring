package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event class representing a delete action on an order item editor.
 * This event is fired when a user requests to delete an order item from an order.
 */
public class DeleteEvent extends ComponentEvent<OrderItemEditor> {

    /**
     * Constructs a new DeleteEvent.
     *
     * @param component the OrderItemEditor that triggered the delete event
     */
    public DeleteEvent(OrderItemEditor component) {
        super(component, false);
    }
}