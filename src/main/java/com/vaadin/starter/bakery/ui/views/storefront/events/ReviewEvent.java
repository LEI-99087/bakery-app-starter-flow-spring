package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

/**
 * Event class representing a review action on an order.
 * This event is fired when a user requests to review an order before saving or submitting.
 */
public class ReviewEvent extends ComponentEvent<OrderEditor> {

    /**
     * Constructs a new ReviewEvent.
     *
     * @param component the OrderEditor that triggered the review event
     */
    public ReviewEvent(OrderEditor component) {
        super(component, false);
    }
}