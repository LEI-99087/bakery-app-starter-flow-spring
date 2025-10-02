/**
 * Component for displaying a full (read-only) summary of an order, with a comment field to add comments.
 * Provides functionality to view order details, add comments, and navigate back or edit the order.
 */
package com.vaadin.starter.bakery.ui.views.orderedit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.events.CancelEvent;
import com.vaadin.starter.bakery.ui.events.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontLocalDateConverter;
import com.vaadin.starter.bakery.ui.views.storefront.events.CommentEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.EditEvent;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * The component displaying a full (read-only) summary of an order, and a comment
 * field to add comments.
 */
@Tag("order-details")
@JsModule("./src/views/orderedit/order-details.js")
public class OrderDetails extends LitTemplate {

    private Order order;

    @Id("back")
    private Button back;

    @Id("cancel")
    private Button cancel;

    @Id("save")
    private Button save;

    @Id("edit")
    private Button edit;

    @Id("history")
    private Element history;

    @Id("comment")
    private Element comment;

    @Id("sendComment")
    private Button sendComment;

    @Id("commentField")
    private TextField commentField;

    private boolean isDirty;

    /**
     * Constructs a new OrderDetails component with event listeners for user interactions.
     */
    public OrderDetails() {
        sendComment.addClickListener(e -> {
            String message = commentField.getValue();
            message = message == null ? "" : message.trim();
            if (!message.isEmpty()) {
                commentField.clear();
                fireEvent(new CommentEvent(this, order.getId(), message));
            }
        });
        save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        edit.addClickListener(e -> fireEvent(new EditEvent(this)));
    }

    /**
     * Displays the order details in the component.
     *
     * @param order the order to display
     * @param review true if in review mode, false if in edit mode
     */
    public void display(Order order, boolean review) {
        getElement().setProperty("review", review);
        this.order = order;

        JsonObject item = beanToJson(order);

        // Include formatted values to the JsonObject
        item.put("formattedDueDate", beanToJson(new StorefrontLocalDateConverter().encode(order.getDueDate())));
        item.put("formattedDueTime", new LocalTimeConverter().encode(order.getDueTime()));
        item.put("formattedTotalPrice", new CurrencyFormatter().encode(order.getTotalPrice()));

        JsonArray orderItems = item.getArray("items");
        for (int i = 0; i < orderItems.length(); i++) {
            JsonObject itemProduct = orderItems.getObject(i).getObject("product");
            Product product = order.getItems().get(i).getProduct();
            itemProduct.put("formattedPrice", new CurrencyFormatter().encode(product.getPrice()));
        }

        JsonArray orderHistory = item.getArray("history");
        for (int i = 0; i < orderHistory.length(); i++) {
            JsonObject itemHistory = orderHistory.getObject(i);
            HistoryItem historyItem = order.getHistory().get(i);
            itemHistory.put("formattedTimestamp", new LocalDateTimeConverter().encode(historyItem.getTimestamp()));
        }

        getElement().setPropertyJson("item", item);

        if (!review) {
            commentField.clear();
        }
        this.isDirty = review;
    }

    /**
     * Converts a Java bean to a JsonObject using Jackson ObjectMapper.
     * Workaround for https://github.com/vaadin/flow/issues/13317
     *
     * @param bean the Java object to convert
     * @return the converted JsonObject, or null if conversion fails
     */
    private JsonObject beanToJson(Object bean) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return Json.parse(objectMapper.writeValueAsString(bean));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the order details have unsaved changes.
     *
     * @return true if there are unsaved changes, false otherwise
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * Sets the dirty flag indicating whether there are unsaved changes.
     *
     * @param isDirty true if there are unsaved changes, false otherwise
     */
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    /**
     * Adds a listener for save events.
     *
     * @param listener the listener to be notified when save is triggered
     * @return a registration for the listener
     */
    public Registration addSaveListenter(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    /**
     * Adds a listener for edit events.
     *
     * @param listener the listener to be notified when edit is triggered
     * @return a registration for the listener
     */
    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    /**
     * Adds a listener for back button click events.
     *
     * @param listener the listener to be notified when back is clicked
     * @return a registration for the listener
     */
    public Registration addBackListener(ComponentEventListener<ClickEvent<Button>> listener) {
        return back.addClickListener(listener);
    }

    /**
     * Adds a listener for comment events.
     *
     * @param listener the listener to be notified when a comment is added
     * @return a registration for the listener
     */
    public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
        return addListener(CommentEvent.class, listener);
    }

    /**
     * Adds a listener for cancel events.
     *
     * @param listener the listener to be notified when cancel is triggered
     * @return a registration for the listener
     */
    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}