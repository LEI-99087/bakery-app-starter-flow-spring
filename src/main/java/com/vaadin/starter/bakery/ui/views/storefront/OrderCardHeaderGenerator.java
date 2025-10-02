package com.vaadin.starter.bakery.ui.views.storefront;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;

/**
 * Generator for order card headers that groups orders by date ranges.
 * Creates visual separators between orders based on their due dates
 * to improve organization and readability in the storefront view.
 */
public class OrderCardHeaderGenerator {

    private class HeaderWrapper {
        private Predicate<LocalDate> matcher;

        private OrderCardHeader header;

        private Long selected;

        public HeaderWrapper(Predicate<LocalDate> matcher, OrderCardHeader header) {
            this.matcher = matcher;
            this.header = header;
        }

        public boolean matches(LocalDate date) {
            return matcher.test(date);
        }

        public Long getSelected() {
            return selected;
        }

        public void setSelected(Long selected) {
            this.selected = selected;
        }

        public OrderCardHeader getHeader() {
            return header;
        }
    }

    private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

    private final Map<Long, OrderCardHeader> ordersWithHeaders = new HashMap<>();
    private List<HeaderWrapper> headerChain = new ArrayList<>();

    /**
     * Creates a header for the "Recent" orders section.
     *
     * @return OrderCardHeader for recent orders
     */
    private OrderCardHeader getRecentHeader() {
        return new OrderCardHeader("Recent", "Before this week");
    }

    /**
     * Creates a header for the "Yesterday" orders section.
     *
     * @return OrderCardHeader for yesterday's orders
     */
    private OrderCardHeader getYesterdayHeader() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return new OrderCardHeader("Yesterday", secondaryHeaderFor(yesterday));
    }

    /**
     * Creates a header for the "Today" orders section.
     *
     * @return OrderCardHeader for today's orders
     */
    private OrderCardHeader getTodayHeader() {
        LocalDate today = LocalDate.now();
        return new OrderCardHeader("Today", secondaryHeaderFor(today));
    }

    /**
     * Creates a header for orders from this week before yesterday.
     *
     * @return OrderCardHeader for this week's orders before yesterday
     */
    private OrderCardHeader getThisWeekBeforeYesterdayHeader() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return new OrderCardHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
    }

    /**
     * Creates a header for orders from this week starting tomorrow.
     *
     * @param showPrevious whether to show previous orders
     * @return OrderCardHeader for this week's orders starting tomorrow
     */
    private OrderCardHeader getThisWeekStartingTomorrow(boolean showPrevious) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
        return new OrderCardHeader(showPrevious ? "This week starting tomorrow" : "This week",
                secondaryHeaderFor(tomorrow, nextWeekStart));
    }

    /**
     * Creates a header for the "Upcoming" orders section.
     *
     * @return OrderCardHeader for upcoming orders
     */
    private OrderCardHeader getUpcomingHeader() {
        return new OrderCardHeader("Upcoming", "After this week");
    }

    /**
     * Formats a single date for the secondary header.
     *
     * @param date the date to format
     * @return formatted date string
     */
    private String secondaryHeaderFor(LocalDate date) {
        return HEADER_DATE_TIME_FORMATTER.format(date);
    }

    /**
     * Formats a date range for the secondary header.
     *
     * @param start the start date
     * @param end the end date
     * @return formatted date range string
     */
    private String secondaryHeaderFor(LocalDate start, LocalDate end) {
        return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
    }

    /**
     * Gets the header for a specific order ID.
     *
     * @param id the order ID
     * @return the OrderCardHeader for the order, or null if not found
     */
    public OrderCardHeader get(Long id) {
        return ordersWithHeaders.get(id);
    }

    /**
     * Resets the header chain based on whether previous orders should be shown.
     *
     * @param showPrevious true to include previous orders in the header chain
     */
    public void resetHeaderChain(boolean showPrevious) {
        this.headerChain = createHeaderChain(showPrevious);
        ordersWithHeaders.clear();
    }

    /**
     * Processes a list of orders and assigns headers based on their due dates.
     *
     * @param orders the list of orders to process
     */
    public void ordersRead(List<Order> orders) {
        Iterator<HeaderWrapper> headerIterator = headerChain.stream().filter(h -> h.getSelected() == null).iterator();
        if (!headerIterator.hasNext()) {
            return;
        }

        HeaderWrapper current = headerIterator.next();
        for (Order order : orders) {
            // If last selected, discard orders that match it.
            if (current.getSelected() != null && current.matches(order.getDueDate())) {
                continue;
            }
            while (current != null && !current.matches(order.getDueDate())) {
                current = headerIterator.hasNext() ? headerIterator.next() : null;
            }
            if (current == null) {
                break;
            }
            current.setSelected(order.getId());
            ordersWithHeaders.put(order.getId(), current.getHeader());
        }
    }

    /**
     * Creates the header chain based on date ranges and showPrevious setting.
     *
     * @param showPrevious whether to include headers for previous orders
     * @return list of HeaderWrapper objects representing the header chain
     */
    private List<HeaderWrapper> createHeaderChain(boolean showPrevious) {
        List<HeaderWrapper> headerChain = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfTheWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        if (showPrevious) {
            LocalDate yesterday = today.minusDays(1);
            // Week starting on Monday
            headerChain.add(new HeaderWrapper(d -> d.isBefore(startOfTheWeek), this.getRecentHeader()));
            if (startOfTheWeek.isBefore(yesterday)) {
                headerChain.add(new HeaderWrapper(d -> d.isBefore(yesterday) && !d.isAfter(startOfTheWeek),
                        this.getThisWeekBeforeYesterdayHeader()));
            }
            headerChain.add(new HeaderWrapper(yesterday::equals, this.getYesterdayHeader()));
        }
        LocalDate firstDayOfTheNextWeek = startOfTheWeek.plusDays(7);
        headerChain.add(new HeaderWrapper(today::equals, getTodayHeader()));
        headerChain.add(new HeaderWrapper(d -> d.isAfter(today) && d.isBefore(firstDayOfTheNextWeek),
                getThisWeekStartingTomorrow(showPrevious)));
        headerChain.add(new HeaderWrapper(d -> !d.isBefore(firstDayOfTheNextWeek), getUpcomingHeader()));
        return headerChain;
    }
}