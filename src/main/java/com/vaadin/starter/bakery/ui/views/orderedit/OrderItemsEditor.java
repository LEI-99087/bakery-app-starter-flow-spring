package com.vaadin.starter.bakery.ui.views.orderedit;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.views.storefront.events.TotalPriceChangeEvent;

/**
 * Editor component for managing multiple order items within an order.
 * Provides functionality to add, remove, and edit order items with real-time price calculation.
 */
public class OrderItemsEditor extends Div implements HasValueAndElement<ComponentValueChangeEvent<OrderItemsEditor,List<OrderItem>>, List<OrderItem>> {

    private OrderItemEditor empty;

    private DataProvider<Product, String> productDataProvider;

    private int totalPrice = 0;

    private boolean hasChanges = false;

    private final AbstractFieldSupport<OrderItemsEditor,List<OrderItem>> fieldSupport;

    /**
     * Constructs a new OrderItemsEditor with the specified product data provider.
     *
     * @param productDataProvider the data provider for product selection
     */
    public OrderItemsEditor(DataProvider<Product, String> productDataProvider) {
        this.productDataProvider = productDataProvider;
        this.fieldSupport = new AbstractFieldSupport<>(this, Collections.emptyList(),
                Objects::equals, c ->  {});
    }

    /**
     * Sets the list of order items to be edited.
     *
     * @param items the list of OrderItem objects to display and edit
     */
    @Override
    public void setValue(List<OrderItem> items) {
        fieldSupport.setValue(items);
        removeAll();
        totalPrice = 0;
        hasChanges = false;

        if (items != null) {
            items.forEach(this::createEditor);
        }
        createEmptyElement();
        setHasChanges(false);
    }

    /**
     * Creates an OrderItemEditor for the specified OrderItem value.
     *
     * @param value the OrderItem to edit, or null for an empty editor
     * @return the created OrderItemEditor instance
     */
    private OrderItemEditor createEditor(OrderItem value) {
        OrderItemEditor editor = new OrderItemEditor(productDataProvider);
        getElement().appendChild(editor.getElement());
        editor.addPriceChangeListener(e -> updateTotalPriceOnItemPriceChange(e.getOldValue(), e.getNewValue()));
        editor.addProductChangeListener(e -> productChanged(e.getSource(), e.getProduct()));
        editor.addCommentChangeListener(e -> setHasChanges(true));
        editor.addDeleteListener(e -> {
            OrderItemEditor orderItemEditor = e.getSource();
            if (orderItemEditor != empty) {
                remove(orderItemEditor);
                OrderItem orderItem = orderItemEditor.getValue();
                setValue(getValue().stream().filter(element -> element != orderItem).collect(Collectors.toList()));
                updateTotalPriceOnItemPriceChange(orderItem.getTotalPrice(), 0);
                setHasChanges(true);
            }
        });

        editor.setValue(value);
        return editor;
    }

    /**
     * Sets the read-only state for all order item editors.
     *
     * @param readOnly true to set read-only mode, false for editable mode
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        HasValueAndElement.super.setReadOnly(readOnly);
        getChildren().forEach(e -> ((OrderItemEditor) e).setReadOnly(readOnly));
    }

    /**
     * Gets the current list of order items.
     *
     * @return the list of OrderItem objects
     */
    @Override
    public List<OrderItem> getValue() {
        return fieldSupport.getValue();
    }

    /**
     * Handles product change events from order item editors.
     * Creates a new empty element when a product is selected in the empty editor.
     *
     * @param item the OrderItemEditor that triggered the change
     * @param product the selected product
     */
    private void productChanged(OrderItemEditor item, Product product) {
        setHasChanges(true);
        if (empty == item) {
            createEmptyElement();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            item.setValue(orderItem);
            setValue(Stream.concat(getValue().stream(),Stream.of(orderItem)).collect(Collectors.toList()));
        }
    }

    /**
     * Updates the total price when an individual item's price changes.
     *
     * @param oldItemPrice the previous price of the item
     * @param newItemPrice the new price of the item
     */
    private void updateTotalPriceOnItemPriceChange(int oldItemPrice, int newItemPrice) {
        final int delta = newItemPrice - oldItemPrice;
        totalPrice += delta;
        setHasChanges(true);
        fireEvent(new TotalPriceChangeEvent(this, totalPrice));
    }

    /**
     * Creates an empty order item editor for adding new items.
     */
    private void createEmptyElement() {
        empty = createEditor(null);
    }

    /**
     * Adds a listener for total price change events.
     *
     * @param listener the listener to be notified when total price changes
     * @return a registration for the listener
     */
    public Registration addPriceChangeListener(ComponentEventListener<TotalPriceChangeEvent> listener) {
        return addListener(TotalPriceChangeEvent.class, listener);
    }

    /**
     * Checks if there are unsaved changes in the order items.
     *
     * @return true if there are unsaved changes, false otherwise
     */
    public boolean hasChanges() {
        return hasChanges;
    }

    /**
     * Sets the hasChanges flag and fires a ValueChangeEvent if changes are detected.
     *
     * @param hasChanges true if there are changes, false otherwise
     */
    private void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
        if (hasChanges) {
            fireEvent(new com.vaadin.starter.bakery.ui.views.storefront.events.ValueChangeEvent(this));
        }
    }

    /**
     * Validates all order item editors and returns a stream of fields with validation errors.
     *
     * @return a stream of fields that have validation errors
     */
    public Stream<HasValue<?, ?>> validate() {
        return getChildren()
                .filter(component -> fieldSupport.getValue().size() == 0 || !component.equals(empty))
                .map(editor -> ((OrderItemEditor) editor).validate()).flatMap(stream -> stream);
    }

    /**
     * Adds a listener for value change events.
     *
     * @param listener the listener to be notified when the value changes
     * @return a registration for the listener
     */
    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super ComponentValueChangeEvent<OrderItemsEditor, List<OrderItem>>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}