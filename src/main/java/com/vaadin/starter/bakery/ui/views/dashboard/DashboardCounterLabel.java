package com.vaadin.starter.bakery.ui.views.dashboard;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrdersCountData;

/**
 * Custom LitTemplate component for displaying order count information on the dashboard.
 * Shows a title, subtitle, and count value in a formatted layout.
 */
@Tag("dashboard-counter-label")
@JsModule("./src/views/dashboard/dashboard-counter-label.js")
public class DashboardCounterLabel extends LitTemplate {

    @Id("title")
    private H4 title;

    @Id("subtitle")
    private Div subtitle;

    @Id("count")
    private Span count;

    /**
     * Sets the order count data to be displayed in the component.
     *
     * @param data the OrdersCountData containing title, subtitle, and count information
     */
    public void setOrdersCountData(OrdersCountData data) {
        title.setText(data.getTitle());
        subtitle.setText(data.getSubtitle());
        count.setText(String.valueOf(data.getCount()));
    }
}