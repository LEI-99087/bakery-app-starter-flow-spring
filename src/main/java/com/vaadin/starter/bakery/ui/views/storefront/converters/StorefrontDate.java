package com.vaadin.starter.bakery.ui.views.storefront.converters;

import java.io.Serializable;

/**
 * Represents formatted date information for display in the storefront view.
 * Contains separate fields for day, weekday, and full date formatting.
 */
public class StorefrontDate implements Serializable {
    private String day;
    private String weekday;
    private String date;

    /**
     * Gets the full formatted date string.
     *
     * @return the formatted date string
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the full formatted date string.
     *
     * @param date the formatted date string to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the day portion of the date.
     *
     * @return the day string
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day portion of the date.
     *
     * @param day the day string to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Gets the weekday portion of the date.
     *
     * @return the weekday string
     */
    public String getWeekday() {
        return weekday;
    }

    /**
     * Sets the weekday portion of the date.
     *
     * @param weekday the weekday string to set
     */
    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

}