package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Extends OrdersCountData to include overall count information for chart visualization.
 * Used for displaying order count data with additional context for chart components.
 */
public class OrdersCountDataWithChart extends OrdersCountData {

    private Integer overall;

    /**
     * Constructs an empty OrdersCountDataWithChart instance.
     */
    public OrdersCountDataWithChart() {

    }

    /**
     * Constructs an OrdersCountDataWithChart instance with the specified values.
     *
     * @param title the title text
     * @param subtitle the subtitle text
     * @param count the current order count value
     * @param overall the overall/total order count for chart context
     */
    public OrdersCountDataWithChart(String title, String subtitle, Integer count, Integer overall) {
        super(title, subtitle, count);
        this.overall = overall;
    }

    /**
     * Gets the overall order count used for chart visualization.
     *
     * @return the overall order count
     */
    public Integer getOverall() {
        return overall;
    }

    /**
     * Sets the overall order count used for chart visualization.
     *
     * @param overall the overall order count to set
     */
    public void setOverall(Integer overall) {
        this.overall = overall;
    }

}