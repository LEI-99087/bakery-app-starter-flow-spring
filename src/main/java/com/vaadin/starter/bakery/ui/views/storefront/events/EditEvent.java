package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event class representing an edit action in the storefront.
 * This event is fired when a user initiates an edit operation on an order or component.
 */
public class EditEvent extends ComponentEvent<Component> {

    /**
     * Constructs a new EditEvent.
     *
     * @param source the component that triggered the edit event
     */
    public EditEvent(Component source) {
        super(source, false);
    }
}