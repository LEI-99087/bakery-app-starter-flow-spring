package com.vaadin.starter.bakery.backend.data;

/**
 * Represents delivery statistics for the bakery dashboard.
 * <p>
 * Contains counts for various delivery-related metrics used to track
 * order fulfillment performance and workload.
 * </p>
 */
public class DeliveryStats {

    private int deliveredToday;
    private int dueToday;
    private int dueTomorrow;
    private int notAvailableToday;
    private int newOrders;

    /**
     * Gets the number of orders delivered today.
     *
     * @return count of orders delivered today
     */
    public int getDeliveredToday() {
        return deliveredToday;
    }

    /**
     * Sets the number of orders delivered today.
     *
     * @param deliveredToday count of orders delivered today
     */
    public void setDeliveredToday(int deliveredToday) {
        this.deliveredToday = deliveredToday;
    }

    /**
     * Gets the number of orders due for delivery today.
     *
     * @return count of orders due today
     */
    public int getDueToday() {
        return dueToday;
    }

    /**
     * Sets the number of orders due for delivery today.
     *
     * @param dueToday count of orders due today
     */
    public void setDueToday(int dueToday) {
        this.dueToday = dueToday;
    }

    /**
     * Gets the number of orders due for delivery tomorrow.
     *
     * @return count of orders due tomorrow
     */
    public int getDueTomorrow() {
        return dueTomorrow;
    }

    /**
     * Sets the number of orders due for delivery tomorrow.
     *
     * @param dueTomorrow count of orders due tomorrow
     */
    public void setDueTomorrow(int dueTomorrow) {
        this.dueTomorrow = dueTomorrow;
    }

    /**
     * Gets the number of orders not available for delivery today.
     *
     * @return count of orders not available today
     */
    public int getNotAvailableToday() {
        return notAvailableToday;
    }

    /**
     * Sets the number of orders not available for delivery today.
     *
     * @param notAvailableToday count of orders not available today
     */
    public void setNotAvailableToday(int notAvailableToday) {
        this.notAvailableToday = notAvailableToday;
    }

    /**
     * Gets the number of new orders received.
     *
     * @return count of new orders
     */
    public int getNewOrders() {
        return newOrders;
    }

    /**
     * Sets the number of new orders received.
     *
     * @param newOrders count of new orders
     */
    public void setNewOrders(int newOrders) {
        this.newOrders = newOrders;
    }

}