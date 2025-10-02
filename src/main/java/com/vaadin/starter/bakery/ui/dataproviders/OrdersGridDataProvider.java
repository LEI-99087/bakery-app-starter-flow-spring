package com.vaadin.starter.bakery.ui.dataproviders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * A pageable order data provider for the orders grid.
 * Provides paginated, filterable, and sortable data access for Order entities.
 */
@SpringComponent
@UIScope
public class OrdersGridDataProvider extends FilterablePageableDataProvider<Order, OrdersGridDataProvider.OrderFilter> {

    /**
     * Filter class for order data provider containing text filter and date range settings.
     */
    public static class OrderFilter implements Serializable {
        private String filter;
        private boolean showPrevious;

        /**
         * Gets the text filter value.
         *
         * @return the filter text
         */
        public String getFilter() {
            return filter;
        }

        /**
         * Checks if previous orders should be shown.
         *
         * @return true if previous orders should be shown, false otherwise
         */
        public boolean isShowPrevious() {
            return showPrevious;
        }

        /**
         * Constructs a new OrderFilter with the specified parameters.
         *
         * @param filter the text filter to apply
         * @param showPrevious whether to show previous orders
         */
        public OrderFilter(String filter, boolean showPrevious) {
            this.filter = filter;
            this.showPrevious = showPrevious;
        }

        /**
         * Creates an empty filter that shows no previous orders.
         *
         * @return an empty OrderFilter instance
         */
        public static OrderFilter getEmptyFilter() {
            return new OrderFilter("", false);
        }
    }

    private final OrderService orderService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<Order>> pageObserver;

    /**
     * Constructs a new OrdersGridDataProvider with the specified OrderService.
     *
     * @param orderService the service used for order data operations
     */
    @Autowired
    public OrdersGridDataProvider(OrderService orderService) {
        this.orderService = orderService;
        setSortOrders(BakeryConst.DEFAULT_SORT_DIRECTION, BakeryConst.ORDER_SORT_FIELDS);
    }

    /**
     * Sets up the default sort orders for the data provider.
     *
     * @param direction the sort direction (ascending or descending)
     * @param properties the properties to sort by
     */
    private void setSortOrders(Sort.Direction direction, String[] properties) {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        for (String property : properties) {
            if (direction.isAscending()) {
                builder.thenAsc(property);
            } else {
                builder.thenDesc(property);
            }
        }
        defaultSortOrders = builder.build();
    }

    /**
     * Fetches a page of orders from the backend service matching the given query and pagination.
     *
     * @param query the query containing filter and sorting information
     * @param pageable the pagination information
     * @return a page of orders matching the criteria
     */
    @Override
    protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
        Page<Order> page = orderService.findAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()),
                getFilterDate(filter.isShowPrevious()), pageable);
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        return page;
    }

    /**
     * Gets the default sort orders for the data provider.
     *
     * @return the list of default query sort orders
     */
    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    /**
     * Gets the total count of orders matching the given filter criteria.
     *
     * @param query the query containing filter information
     * @return the total count of matching orders
     */
    @Override
    protected int sizeInBackEnd(Query<Order, OrderFilter> query) {
        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
        return (int) orderService
                .countAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()), getFilterDate(filter.isShowPrevious()));
    }

    /**
     * Gets the filter date based on whether previous orders should be shown.
     *
     * @param showPrevious true if previous orders should be shown
     * @return an Optional containing the filter date, or empty if showing previous orders
     */
    private Optional<LocalDate> getFilterDate(boolean showPrevious) {
        if (showPrevious) {
            return Optional.empty();
        }

        return Optional.of(LocalDate.now().minusDays(1));
    }

    /**
     * Sets a page observer that will be notified whenever a page of orders is fetched.
     *
     * @param pageObserver the consumer to be notified with the fetched page
     */
    public void setPageObserver(Consumer<Page<Order>> pageObserver) {
        this.pageObserver = pageObserver;
    }

    /**
     * Gets the unique identifier for an order item.
     *
     * @param item the order item
     * @return the order ID as the unique identifier
     */
    @Override
    public Object getId(Order item) {
        return item.getId();
    }
}