package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents an individual item within an order.
 * <p>
 * Each OrderItem contains a product, quantity, and optional comment
 * for special instructions or modifications.
 * </p>
 */
@Entity
public class OrderItem extends AbstractEntity {

    @ManyToOne
    @NotNull(message = "{bakery.pickup.product.required}")
    private Product product;

    @Min(1)
    @NotNull
    private Integer quantity = 1;

    @Size(max = 255)
    private String comment;

    /**
     * Gets the product associated with this order item.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product for this order item.
     *
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the quantity of the product in this order item.
     *
     * @return the quantity (minimum 1)
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in this order item.
     * The quantity must be at least 1.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the comment or special instructions for this order item.
     *
     * @return the comment, or null if none
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment or special instructions for this order item.
     *
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Calculates the total price for this order item.
     * The total price is the product price multiplied by the quantity.
     *
     * @return the total price for this order item, or 0 if quantity or product is null
     */
    public int getTotalPrice() {
        return quantity == null || product == null ? 0 : quantity * product.getPrice();
    }
}