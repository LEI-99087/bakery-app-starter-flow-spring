package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;

/**
 * A custom search bar component that provides text filtering and optional checkbox functionality.
 * This component combines a text field for search input, a clear button, and an action button
 * with optional checkbox support for additional filtering options.
 */
@Tag("search-bar")
@JsModule("./src/components/search-bar.js")
public class SearchBar extends LitTemplate {

    @Id("field")
    private TextField textField;

    @Id("clear")
    private Button clearButton;

    @Id("action")
    private Button actionButton;

    /**
     * Constructs a new SearchBar with default configuration.
     * Sets up value change mode and event listeners for filtering functionality.
     */
    public SearchBar() {
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        ComponentUtil.addListener(textField, SearchValueChanged.class,
                e -> fireEvent(new FilterChanged(this, false)));

        clearButton.addClickListener(e -> {
            textField.clear();
            getElement().setProperty("checkboxChecked", false);
        });

        getElement().addPropertyChangeListener("checkboxChecked", e -> fireEvent(new FilterChanged(this, false)));
    }

    /**
     * Gets the current filter text from the search field.
     *
     * @return the current filter text, or empty string if no filter is set
     */
    public String getFilter() {
        return textField.getValue();
    }

    /**
     * Gets the current state of the checkbox.
     *
     * @return true if the checkbox is checked, false otherwise
     */
    @Synchronize("checkbox-checked-changed")
    public boolean isCheckboxChecked() {
        return getElement().getProperty("checkboxChecked", false);
    }

    /**
     * Sets the placeholder text for the search field.
     *
     * @param placeHolder the placeholder text to display in the search field
     */
    public void setPlaceHolder(String placeHolder) {
        textField.setPlaceholder(placeHolder);
    }

    /**
     * Sets the text for the action button.
     *
     * @param actionText the text to display on the action button
     */
    public void setActionText(String actionText) {
        getElement().setProperty("buttonText", actionText);
    }

    /**
     * Sets the text for the checkbox label.
     *
     * @param checkboxText the text to display next to the checkbox
     */
    public void setCheckboxText(String checkboxText) {
        getElement().setProperty("checkboxText", checkboxText);
    }

    /**
     * Adds a listener for filter change events.
     * Triggered when the search text changes or the checkbox state changes.
     *
     * @param listener the listener to be notified of filter changes
     */
    public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
        this.addListener(FilterChanged.class, listener);
    }

    /**
     * Adds a listener for action button click events.
     *
     * @param listener the listener to be notified when the action button is clicked
     */
    public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        actionButton.addClickListener(listener);
    }

    /**
     * Gets the action button instance.
     *
     * @return the action button component
     */
    public Button getActionButton() {
        return actionButton;
    }

    /**
     * Event class representing a value change in the search text field.
     * Uses debouncing to reduce event frequency during rapid typing.
     */
    @DomEvent(value = "value-changed", debounce = @DebounceSettings(timeout = 300, phases = DebouncePhase.TRAILING))
    public static class SearchValueChanged extends ComponentEvent<TextField> {
        /**
         * Constructs a new SearchValueChanged event.
         *
         * @param source the text field that triggered the event
         * @param fromClient true if the event originated from the client side
         */
        public SearchValueChanged(TextField source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Event class representing a filter change in the search bar.
     * Triggered by text field changes or checkbox state changes.
     */
    public static class FilterChanged extends ComponentEvent<SearchBar> {
        /**
         * Constructs a new FilterChanged event.
         *
         * @param source the search bar that triggered the event
         * @param fromClient true if the event originated from the client side
         */
        public FilterChanged(SearchBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}