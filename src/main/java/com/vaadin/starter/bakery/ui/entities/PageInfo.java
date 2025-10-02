package com.vaadin.starter.bakery.ui.entities;

/**
 * Represents information about a page in the application.
 * Contains navigation link, icon, and title for menu items or page references.
 */
public class PageInfo {
    private final String link;
    private final String icon;
    private final String title;

    /**
     * Constructs a new PageInfo with the specified properties.
     *
     * @param link the navigation link for the page
     * @param icon the icon identifier for the page
     * @param title the display title for the page
     */
    public PageInfo(String link, String icon, String title) {
        this.link = link;
        this.icon = icon;
        this.title = title;
    }

    /**
     * Gets the navigation link for the page.
     *
     * @return the page navigation link
     */
    public String getLink() {
        return link;
    }

    /**
     * Gets the icon identifier for the page.
     *
     * @return the page icon identifier
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Gets the display title for the page.
     *
     * @return the page display title
     */
    public String getTitle() {
        return title;
    }

}