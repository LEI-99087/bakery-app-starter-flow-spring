package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Represents order count data for display in the storefront dashboard.
 * Contains title, subtitle, and count information for order statistics.
 */
public class OrdersCountData {

    private String title;
    private String subtitle;
    private Integer count;

    /**
     * Gets the title of the order count display.
     *
     * @return the title text
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the order count display.
     *
     * @param title the title text to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the subtitle of the order count display.
     *
     * @return the subtitle text
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the subtitle of the order count display.
     *
     * @param subtitle the subtitle text to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Gets the order count value.
     *
     * @return the order count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the order count value.
     *
     * @param count the order count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Constructs an empty OrdersCountData instance.
     */
    public OrdersCountData() {

    }

    /**
     * Constructs an OrdersCountData instance with the specified values.
     *
     * @param title the title text
     * @param subtitle the subtitle text
     * @param count the order count value
     */
    public OrdersCountData(String title, String subtitle, Integer count) {
        this.title = title;
        this.subtitle = subtitle;
        this.count = count;
    }

}