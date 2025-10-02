package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event class representing a comment change in an order item editor.
 * This event is fired when the comment field of an order item is modified.
 */
public class CommentChangeEvent extends ComponentEvent<OrderItemEditor> {

    private final String comment;

    /**
     * Constructs a new CommentChangeEvent.
     *
     * @param component the OrderItemEditor that triggered the event
     * @param comment the new comment value
     */
    public CommentChangeEvent(OrderItemEditor component, String comment) {
        super(component, false);
        this.comment = comment;
    }

    /**
     * Gets the comment value that triggered the event.
     *
     * @return the comment text
     */
    public String getComment() {
        return comment;
    }

}