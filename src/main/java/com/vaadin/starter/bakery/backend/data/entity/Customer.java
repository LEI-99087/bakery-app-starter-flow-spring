package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents a customer entity in the bakery system.
 * <p>
 * Stores customer information including name, contact details, and additional notes.
 * </p>
 */
@Entity
public class Customer extends AbstractEntity {

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Size(max = 20, message = "{bakery.phone.number.invalid}")
    // A simple phone number checker, allowing an optional international prefix
    // plus a variable number of digits that could be separated by dashes or
    // spaces
    @Pattern(regexp = "^(\\+\\d+)?([ -]?\\d+){4,14}$", message = "{bakery.phone.number.invalid}")
    private String phoneNumber;

    @Size(max = 255)
    private String details;

    /**
     * Gets the customer's full name.
     *
     * @return the customer's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the customer's full name.
     *
     * @param fullName the full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the customer's phone number.
     *
     * @return the customer's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the customer's phone number.
     * <p>
     * The phone number must match the pattern for valid phone numbers
     * and cannot exceed 20 characters.
     * </p>
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets additional details about the customer.
     *
     * @return customer details or notes
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the customer.
     *
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

}