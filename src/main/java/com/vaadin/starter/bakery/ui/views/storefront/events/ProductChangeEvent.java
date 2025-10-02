package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event class representing a product change in an order item editor.
 * This event is fired when a different product is selected for an order item.
 */
public class ProductChangeEvent extends ComponentEvent<OrderItemEditor> {

    private final Product product;

    /**
     * Constructs a new ProductChangeEvent.
     *
     * @param component the OrderItemEditor that triggered the product change
     * @param product the newly selected product
     */
    public ProductChangeEvent(OrderItemEditor component, Product product) {
        super(component, false);
        this.product = product;
    }

    /**
     * Gets the product that was selected in the order item editor.
     *
     * @return the selected product
     */
    public Product getProduct() {
        return product;
    }

}