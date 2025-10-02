package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDetails;

/**
 * Event class representing a comment being added to an order.
 * This event is fired when a user submits a comment for an order in the order details view.
 */
public class CommentEvent extends ComponentEvent<OrderDetails> {

    private Long orderId;
    private String message;

    /**
     * Constructs a new CommentEvent.
     *
     * @param component the OrderDetails component that triggered the event
     * @param orderId the ID of the order the comment is for
     * @param message the comment message text
     */
    public CommentEvent(OrderDetails component, Long orderId, String message) {
        super(component, false);
        this.orderId = orderId;
        this.message = message;
    }

    /**
     * Gets the ID of the order associated with the comment.
     *
     * @return the order ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Gets the comment message text.
     *
     * @return the comment message
     */
    public String getMessage() {
        return message;
    }
}