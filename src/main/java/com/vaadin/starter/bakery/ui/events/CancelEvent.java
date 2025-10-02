package com.vaadin.staker.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event class representing a cancellation action in the UI.
 * This event is fired when a user cancels an operation or dialog.
 */
public class CancelEvent extends ComponentEvent<Component> {

    /**
     * Constructs a new CancelEvent.
     *
     * @param source the component that fired the event
     * @param fromClient true if the event originated from the client side
     */
    public CancelEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }
}