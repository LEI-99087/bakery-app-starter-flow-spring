package com.vaadin.starter.bakery.ui.views.dashboard;

import com.vaadin.flow.component.charts.model.DataSeriesItem;

/**
 * Extended DataSeriesItem class that supports radius and inner radius properties.
 * Used for creating customized chart data series items with specific radius settings.
 */
public class DataSeriesItemWithRadius extends DataSeriesItem {

    private String radius;
    private String innerRadius;

    /**
     * Gets the outer radius of the data series item.
     *
     * @return the outer radius value as a string
     */
    public String getRadius() {
        return radius;
    }

    /**
     * Sets the outer radius of the data series item.
     *
     * @param radius the outer radius value to set
     */
    public void setRadius(String radius) {
        this.radius = radius;
        makeCustomized();
    }

    /**
     * Gets the inner radius of the data series item.
     *
     * @return the inner radius value as a string
     */
    public String getInnerRadius() {
        return innerRadius;
    }

    /**
     * Sets the inner radius of the data series item.
     *
     * @param innerRadius the inner radius value to set
     */
    public void setInnerRadius(String innerRadius) {
        this.innerRadius = innerRadius;
        makeCustomized();
    }
}