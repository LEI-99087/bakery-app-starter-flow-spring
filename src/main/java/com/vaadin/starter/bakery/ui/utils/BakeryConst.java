package com.vaadin.starter.bakery.ui.utils;

import java.util.Locale;

import org.springframework.data.domain.Sort;

/**
 * Application constants class containing configuration values and constants
 * used throughout the Bakery application UI.
 */
public class BakeryConst {

    /** The application locale used for formatting dates, numbers, etc. */
    public static final Locale APP_LOCALE = Locale.US;

    /** Parameter name for order ID in URLs. */
    public static final String ORDER_ID = "orderID";

    /** URL segment for edit operations. */
    public static final String EDIT_SEGMENT = "edit";

    /** Root page path. */
    public static final String PAGE_ROOT = "";

    /** Storefront page path. */
    public static final String PAGE_STOREFRONT = "storefront";

    /** Template for storefront order view with order ID parameter. */
    public static final String PAGE_STOREFRONT_ORDER_TEMPLATE =
            PAGE_STOREFRONT + "/:" + ORDER_ID + "?";

    /** Template for storefront order edit view with order ID parameter. */
    public static final String PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE =
            PAGE_STOREFRONT + "/:" + ORDER_ID + "/" + EDIT_SEGMENT;

    /** Pattern for storefront order edit URL with order ID. */
    public static final String PAGE_STOREFRONT_ORDER_EDIT =
            "storefront/%d/edit";

    /** Dashboard page path. */
    public static final String PAGE_DASHBOARD = "dashboard";

    /** Users management page path. */
    public static final String PAGE_USERS = "users";

    /** Products management page path. */
    public static final String PAGE_PRODUCTS = "products";

    /** Title for the storefront page. */
    public static final String TITLE_STOREFRONT = "Storefront";

    /** Title for the dashboard page. */
    public static final String TITLE_DASHBOARD = "Dashboard";

    /** Title for the users management page. */
    public static final String TITLE_USERS = "Users";

    /** Title for the products management page. */
    public static final String TITLE_PRODUCTS = "Products";

    /** Title for logout action. */
    public static final String TITLE_LOGOUT = "Logout";

    /** Title for not found page. */
    public static final String TITLE_NOT_FOUND = "Page was not found";

    /** Default sort fields for order entities. */
    public static final String[] ORDER_SORT_FIELDS = {"dueDate", "dueTime", "id"};

    /** Default sort direction for data tables. */
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    /** Viewport meta tag content for responsive design. */
    public static final String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";

    /** Duration for notification display in milliseconds. Mutable for testing purposes. */
    public static int NOTIFICATION_DURATION = 4000;

}