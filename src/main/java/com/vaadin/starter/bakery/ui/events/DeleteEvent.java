package com.vaadin.starter.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event class representing a delete action in the UI.
 * This event is fired when a user initiates a delete operation.
 */
public class DeleteEvent extends ComponentEvent<Component> {

    /**
     * Constructs a new DeleteEvent.
     *
     * @param source the component that fired the event
     * @param fromClient true if the event originated from the client side
     */
    public DeleteEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }

}