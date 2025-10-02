package com.vaadin.starter.bakery.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * Represents data structure for the bakery dashboard.
 * <p>
 * Contains various statistics and metrics used to display
 * business performance information on the dashboard.
 * </p>
 */
public class DashboardData {

    private DeliveryStats deliveryStats;
    private List<Number> deliveriesThisMonth;
    private List<Number> deliveriesThisYear;
    private Number[][] salesPerMonth;
    private LinkedHashMap<Product, Integer> productDeliveries;

    /**
     * Gets the delivery statistics.
     *
     * @return the delivery statistics
     */
    public DeliveryStats getDeliveryStats() {
        return deliveryStats;
    }

    /**
     * Sets the delivery statistics.
     *
     * @param deliveryStats the delivery statistics to set
     */
    public void setDeliveryStats(DeliveryStats deliveryStats) {
        this.deliveryStats = deliveryStats;
    }

    /**
     * Gets the list of deliveries for the current month.
     *
     * @return list of delivery numbers for this month
     */
    public List<Number> getDeliveriesThisMonth() {
        return deliveriesThisMonth;
    }

    /**
     * Sets the list of deliveries for the current month.
     *
     * @param deliveriesThisMonth list of delivery numbers for this month
     */
    public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
        this.deliveriesThisMonth = deliveriesThisMonth;
    }

    /**
     * Gets the list of deliveries for the current year.
     *
     * @return list of delivery numbers for this year
     */
    public List<Number> getDeliveriesThisYear() {
        return deliveriesThisYear;
    }

    /**
     * Sets the list of deliveries for the current year.
     *
     * @param deliveriesThisYear list of delivery numbers for this year
     */
    public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
        this.deliveriesThisYear = deliveriesThisYear;
    }

    /**
     * Sets the sales data per month.
     * <p>
     * The data is organized as a two-dimensional array where each row
     * represents sales data for a specific month.
     * </p>
     *
     * @param salesPerMonth the sales data per month to set
     */
    public void setSalesPerMonth(Number[][] salesPerMonth) {
        this.salesPerMonth = salesPerMonth;
    }

    /**
     * Gets the sales data for a specific month.
     *
     * @param i the month index
     * @return array of sales numbers for the specified month
     */
    public Number[] getSalesPerMonth(int i) {
        return salesPerMonth[i];
    }

    /**
     * Gets the product delivery counts mapped by product.
     *
     * @return a LinkedHashMap containing products and their delivery counts
     */
    public LinkedHashMap<Product, Integer> getProductDeliveries() {
        return productDeliveries;
    }

    /**
     * Sets the product delivery counts mapped by product.
     *
     * @param productDeliveries a LinkedHashMap containing products and their delivery counts
     */
    public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
        this.productDeliveries = productDeliveries;
    }

}