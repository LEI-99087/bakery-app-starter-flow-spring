package com.vaadin.starter.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event class representing a validation failure in the UI.
 * This event is fired when data validation fails during form submission or data processing.
 */
public class ValidationFailedEvent extends ComponentEvent<Component> {

    /**
     * Constructs a new ValidationFailedEvent.
     * This event is always considered to originate from the server side.
     *
     * @param source the component that fired the event
     */
    public ValidationFailedEvent(Component source) {
        super(source, false);
    }

}