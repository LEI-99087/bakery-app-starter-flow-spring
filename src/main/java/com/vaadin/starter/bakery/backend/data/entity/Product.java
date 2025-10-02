package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * Represents a product available for ordering in the bakery.
 * <p>
 * Each product has a unique name and a price stored as an integer
 * to avoid floating-point rounding errors (real price * 100).
 * </p>
 */
@Entity
public class Product extends AbstractEntity {

    @NotBlank(message = "{bakery.name.required}")
    @Size(max = 255)
    @Column(unique = true)
    private String name;

    // Real price * 100 as an int to avoid rounding errors
    @Min(value = 0, message = "{bakery.price.limits}")
    @Max(value = 100000, message = "{bakery.price.limits}")
    private Integer price;

    /**
     * Gets the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     * <p>
     * The name must be unique across all products and cannot be blank.
     * </p>
     *
     * @param name the product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product in cents.
     * <p>
     * The price is stored as an integer representing the real price multiplied by 100
     * to avoid floating-point rounding errors.
     * </p>
     *
     * @return the price in cents
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Sets the price of the product in cents.
     * <p>
     * The price must be between 0 and 100000 cents (0 to 1000.00 in real currency).
     * </p>
     *
     * @param price the price in cents to set
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the product using its name.
     *
     * @return the product name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this product to another object for equality.
     * Two products are considered equal if they have the same ID, version,
     * name, and price.
     *
     * @param o the reference object with which to compare
     * @return true if this product is the same as the object argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Product that = (Product) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(price, that.price);
    }

    /**
     * Calculates the hash code based on the superclass hash code, name, and price.
     *
     * @return a hash code value for this product
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, price);
    }
}