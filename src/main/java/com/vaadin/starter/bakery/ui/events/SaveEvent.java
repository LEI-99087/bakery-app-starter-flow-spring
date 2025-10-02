package com.vaadin.starter.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event class representing a save action in the UI.
 * This event is fired when a user saves data or confirms an operation.
 */
public class SaveEvent extends ComponentEvent<Component> {

    /**
     * Constructs a new SaveEvent.
     *
     * @param source the component that fired the event
     * @param fromClient true if the event originated from the client side
     */
    public SaveEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }

}