package com.vaadin.starter.bakery.ui.views.orderedit;

import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.views.storefront.events.CommentChangeEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.DeleteEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.PriceChangeEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.ProductChangeEvent;

/**
 * Editor component for individual order items within an order.
 * Allows selection of products, quantity, comments, and displays calculated price.
 */
@Tag("order-item-editor")
@JsModule("./src/views/orderedit/order-item-editor.js")
public class OrderItemEditor extends LitTemplate implements HasValueAndElement<ComponentValueChangeEvent<OrderItemEditor,OrderItem>, OrderItem> {

    @Id("products")
    private ComboBox<Product> products;

    @Id("delete")
    private Button delete;

    @Id("amount")
    private IntegerField amount;

    @Id("price")
    private Div price;

    @Id("comment")
    private TextField comment;

    private int totalPrice;

    private final AbstractFieldSupport<OrderItemEditor,OrderItem> fieldSupport;

    private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

    /**
     * Constructs a new OrderItemEditor with the specified product data provider.
     *
     * @param productDataProvider the data provider for product selection
     */
    public OrderItemEditor(DataProvider<Product, String> productDataProvider) {
        this.fieldSupport =  new AbstractFieldSupport<>(this, null,
                Objects::equals, c ->  {});
        products.setItems(productDataProvider);
        products.addValueChangeListener(e -> {
            setPrice();
            fireEvent(new ProductChangeEvent(this, e.getValue()));
        });
        amount.addValueChangeListener(e -> setPrice());
        comment.addValueChangeListener(e -> fireEvent(new CommentChangeEvent(this, e.getValue())));

        binder.forField(amount).bind("quantity");
        amount.setRequiredIndicatorVisible(true);
        binder.forField(comment).bind("comment");
        binder.forField(products).bind("product");
        products.setRequired(true);

        delete.addClickListener(e -> fireEvent(new DeleteEvent(this)));
        setPrice();
    }

    /**
     * Calculates and sets the price based on selected product and quantity.
     * Fires a PriceChangeEvent if the total price changes.
     */
    private void setPrice() {
        int oldValue = totalPrice;
        Integer selectedAmount = amount.getValue();
        Product product = products.getValue();
        totalPrice = 0;
        if (selectedAmount != null && product != null) {
            totalPrice = selectedAmount * product.getPrice();
        }
        price.setText(FormattingUtils.formatAsCurrency(totalPrice));
        if (oldValue != totalPrice) {
            fireEvent(new PriceChangeEvent(this, oldValue, totalPrice));
        }
    }

    /**
     * Sets the value for this order item editor.
     *
     * @param value the OrderItem to set, or null to clear
     */
    @Override
    public void setValue(OrderItem value) {
        fieldSupport.setValue(value);
        binder.setBean(value);
        boolean noProductSelected = value == null || value.getProduct() == null;
        amount.setEnabled(!noProductSelected);
        delete.setEnabled(!noProductSelected);
        comment.setEnabled(!noProductSelected);
        setPrice();
    }

    /**
     * Gets the current value of this order item editor.
     *
     * @return the current OrderItem value
     */
    @Override
    public OrderItem getValue() {
        return fieldSupport.getValue();
    }

    /**
     * Validates the form fields and returns a stream of fields with validation errors.
     *
     * @return a stream of fields that have validation errors
     */
    public Stream<HasValue<?, ?>> validate() {
        return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
    }

    /**
     * Adds a listener for price change events.
     *
     * @param listener the listener to be notified when price changes
     * @return a registration for the listener
     */
    public Registration addPriceChangeListener(ComponentEventListener<PriceChangeEvent> listener) {
        return addListener(PriceChangeEvent.class, listener);
    }

    /**
     * Adds a listener for product change events.
     *
     * @param listener the listener to be notified when product changes
     * @return a registration for the listener
     */
    public Registration addProductChangeListener(ComponentEventListener<ProductChangeEvent> listener) {
        return addListener(ProductChangeEvent.class, listener);
    }

    /**
     * Adds a listener for comment change events.
     *
     * @param listener the listener to be notified when comment changes
     * @return a registration for the listener
     */
    public Registration addCommentChangeListener(ComponentEventListener<CommentChangeEvent> listener) {
        return addListener(CommentChangeEvent.class, listener);
    }

    /**
     * Adds a listener for delete events.
     *
     * @param listener the listener to be notified when delete is triggered
     * @return a registration for the listener
     */
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    /**
     * Adds a listener for value change events.
     *
     * @param listener the listener to be notified when the value changes
     * @return a registration for the listener
     */
    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super ComponentValueChangeEvent<OrderItemEditor, OrderItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

}