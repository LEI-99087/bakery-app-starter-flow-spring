package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.vaadin.starter.bakery.backend.data.OrderState;

/**
 * Represents an order entity in the bakery system.
 * <p>
 * Stores order information including due date/time, pickup location, customer details,
 * ordered items, order state, and history of state changes.
 * </p>
 */
@Entity(name = "OrderInfo") // "Order" is a reserved word
@NamedEntityGraphs({@NamedEntityGraph(name = Order.ENTITY_GRAPTH_BRIEF, attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode("pickupLocation")
}),@NamedEntityGraph(name = Order.ENTITY_GRAPTH_FULL, attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode("pickupLocation"),
        @NamedAttributeNode("items"),
        @NamedAttributeNode("history")
})})
@Table(indexes = @Index(columnList = "dueDate"))
public class Order extends AbstractEntity implements OrderSummary {

    public static final String ENTITY_GRAPTH_BRIEF = "Order.brief";
    public static final String ENTITY_GRAPTH_FULL = "Order.full";

    @NotNull(message = "{bakery.due.date.required}")
    private LocalDate dueDate;

    @NotNull(message = "{bakery.due.time.required}")
    private LocalTime dueTime;

    @NotNull(message = "{bakery.pickup.location.required}")
    @ManyToOne
    private PickupLocation pickupLocation;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    @JoinColumn
    @BatchSize(size = 1000)
    @NotEmpty
    @Valid
    private List<OrderItem> items;

    @NotNull(message = "{bakery.status.required}")
    private OrderState state;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn
    @JoinColumn
    private List<HistoryItem> history;

    /**
     * Constructs a new Order with the specified creator.
     * Initializes the order with NEW state, creates a customer instance,
     * adds an initial history item, and initializes the items list.
     *
     * @param createdBy the user who created this order
     */
    public Order(User createdBy) {
        this.state = OrderState.NEW;
        setCustomer(new Customer());
        addHistoryItem(createdBy, "Order placed");
        this.items = new ArrayList<>();
    }

    /**
     * Empty constructor is needed by Spring Data / JPA.
     */
    Order() {
        // Empty constructor is needed by Spring Data / JPA
    }

    /**
     * Adds a history item to track order state changes and events.
     *
     * @param createdBy the user who performed the action
     * @param comment the descriptive message for the history item
     */
    public void addHistoryItem(User createdBy, String comment) {
        HistoryItem item = new HistoryItem(createdBy, comment);
        item.setNewState(state);
        if (history == null) {
            history = new LinkedList<>();
        }
        history.add(item);
    }

    /**
     * Gets the due date for this order.
     *
     * @return the due date
     */
    @Override
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date for this order.
     *
     * @param dueDate the due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the due time for this order.
     *
     * @return the due time
     */
    @Override
    public LocalTime getDueTime() {
        return dueTime;
    }

    /**
     * Sets the due time for this order.
     *
     * @param dueTime the due time to set
     */
    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    /**
     * Gets the pickup location for this order.
     *
     * @return the pickup location
     */
    @Override
    public PickupLocation getPickupLocation() {
        return pickupLocation;
    }

    /**
     * Sets the pickup location for this order.
     *
     * @param pickupLocation the pickup location to set
     */
    public void setPickupLocation(PickupLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * Gets the customer associated with this order.
     *
     * @return the customer
     */
    @Override
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer associated with this order.
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the list of items in this order.
     *
     * @return the list of order items
     */
    @Override
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Sets the list of items for this order.
     *
     * @param items the list of order items to set
     */
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * Gets the history of state changes and events for this order.
     *
     * @return the list of history items
     */
    public List<HistoryItem> getHistory() {
        return history;
    }

    /**
     * Sets the history of state changes and events for this order.
     *
     * @param history the list of history items to set
     */
    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

    /**
     * Gets the current state of this order.
     *
     * @return the order state
     */
    @Override
    public OrderState getState() {
        return state;
    }

    /**
     * Changes the state of this order and adds a history item if the state actually changes.
     *
     * @param user the user who changed the state
     * @param state the new state to set
     */
    public void changeState(User user, OrderState state) {
        boolean createHistory = this.state != state && this.state != null && state != null;
        this.state = state;
        if (createHistory) {
            addHistoryItem(user, "Order " + state);
        }
    }

    /**
     * Returns a string representation of this order.
     *
     * @return a string representation of the order
     */
    @Override
    public String toString() {
        return "Order{" + "dueDate=" + dueDate + ", dueTime=" + dueTime + ", pickupLocation=" + pickupLocation
                + ", customer=" + customer + ", items=" + items + ", state=" + state + '}';
    }

    /**
     * Calculates the total price of all items in this order.
     *
     * @return the total price as the sum of all item prices
     */
    @Override
    public Integer getTotalPrice() {
        return items.stream().map(i -> i.getTotalPrice()).reduce(0, Integer::sum);
    }
}