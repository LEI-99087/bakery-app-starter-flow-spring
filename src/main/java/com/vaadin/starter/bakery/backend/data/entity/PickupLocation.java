package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a pickup location where customers can collect their orders.
 * <p>
 * Each pickup location has a unique name that identifies it.
 * </p>
 */
@Entity
public class PickupLocation extends AbstractEntity {

    @Size(max = 255)
    @NotBlank
    @Column(unique = true)
    private String name;

    /**
     * Gets the name of the pickup location.
     *
     * @return the pickup location name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the pickup location.
     * <p>
     * The name must be unique across all pickup locations and cannot be blank.
     * </p>
     *
     * @param name the pickup location name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}