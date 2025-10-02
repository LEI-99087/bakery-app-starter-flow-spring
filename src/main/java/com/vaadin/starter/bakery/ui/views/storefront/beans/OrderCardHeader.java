package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Represents header information for an order card in the storefront view.
 * Contains main and secondary text elements for displaying order header information.
 */
public class OrderCardHeader {

    private String main;
    private String secondary;

    /**
     * Constructs a new OrderCardHeader with the specified main and secondary text.
     *
     * @param main the primary header text
     * @param secondary the secondary header text
     */
    public OrderCardHeader(String main, String secondary) {
        this.main = main;
        this.secondary = secondary;
    }

    /**
     * Gets the main header text.
     *
     * @return the main header text
     */
    public String getMain() {
        return main;
    }

    /**
     * Sets the main header text.
     *
     * @param main the main header text to set
     */
    public void setMain(String main) {
        this.main = main;
    }

    /**
     * Gets the secondary header text.
     *
     * @return the secondary header text
     */
    public String getSecondary() {
        return secondary;
    }

    /**
     * Sets the secondary header text.
     *
     * @param secondary the secondary header text to set
     */
    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}