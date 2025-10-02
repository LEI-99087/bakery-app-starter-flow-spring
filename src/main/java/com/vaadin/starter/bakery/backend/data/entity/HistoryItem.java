package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.vaadin.starter.bakery.backend.data.OrderState;

/**
 * Represents a history item that tracks state changes and events in an order's lifecycle.
 * <p>
 * Each HistoryItem records who performed an action, when it was performed,
 * what state change occurred (if any), and a descriptive message.
 * </p>
 */
@Entity
public class HistoryItem extends AbstractEntity {

    private OrderState newState;

    @NotBlank
    @Size(max = 255)
    private String message;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @NotNull
    private User createdBy;

    /**
     * Empty constructor is needed by Spring Data / JPA.
     */
    HistoryItem() {
        // Empty constructor is needed by Spring Data / JPA
    }

    /**
     * Constructs a new HistoryItem with the specified creator and message.
     * The timestamp is automatically set to the current date and time.
     *
     * @param createdBy the user who created this history item
     * @param message the descriptive message for this history item
     */
    public HistoryItem(User createdBy, String message) {
        this.createdBy = createdBy;
        this.message = message;
        timestamp = LocalDateTime.now();
    }

    /**
     * Gets the new state that was set by this history item.
     *
     * @return the OrderState that was set, or null if no state change occurred
     */
    public OrderState getNewState() {
        return newState;
    }

    /**
     * Sets the new state that was set by this history item.
     *
     * @param newState the OrderState that was set
     */
    public void setNewState(OrderState newState) {
        this.newState = newState;
    }

    /**
     * Gets the descriptive message for this history item.
     *
     * @return the history message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the descriptive message for this history item.
     *
     * @param message the history message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when this history item was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when this history item was created.
     *
     * @param timestamp the creation timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the user who created this history item.
     *
     * @return the user who created this history item
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created this history item.
     *
     * @param createdBy the user who created this history item
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

}