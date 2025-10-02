package com.vaadin.starter.bakery.backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.OrderRepository;

/**
 * Service class for managing Order entities.
 * Provides business logic for order operations including creation, updating, filtering,
 * and dashboard data generation.
 */
@Service
public class OrderService implements CrudService<Order> {

    private final OrderRepository orderRepository;

    /**
     * Constructs an OrderService with the specified OrderRepository.
     *
     * @param orderRepository the repository used for order data access
     */
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        super();
        this.orderRepository = orderRepository;
    }

    private static final Set<OrderState> notAvailableStates = Collections.unmodifiableSet(
            EnumSet.complementOf(EnumSet.of(OrderState.DELIVERED, OrderState.READY, OrderState.CANCELLED)));

    /**
     * Saves an order with the given ID, using a filler function to populate order details.
     * Creates a new order if ID is null, otherwise updates existing order.
     *
     * @param currentUser the user performing the save operation
     * @param id the ID of the order to save, or null to create a new order
     * @param orderFiller a function that populates the order details
     * @return the saved order
     */
    @Transactional(rollbackOn = Exception.class)
    public Order saveOrder(User currentUser, Long id, BiConsumer<User, Order> orderFiller) {
        Order order;
        if (id == null) {
            order = new Order(currentUser);
        } else {
            order = load(id);
        }
        orderFiller.accept(currentUser, order);
        return orderRepository.save(order);
    }

    /**
     * Saves the given order entity.
     *
     * @param order the order to save
     * @return the saved order
     */
    @Transactional(rollbackOn = Exception.class)
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Adds a comment to the specified order and saves it.
     *
     * @param currentUser the user adding the comment
     * @param order the order to add the comment to
     * @param comment the comment text to add
     * @return the updated order with the new comment
     */
    @Transactional(rollbackOn = Exception.class)
    public Order addComment(User currentUser, Order order, String comment) {
        order.addHistoryItem(currentUser, comment);
        return orderRepository.save(order);
    }

    /**
     * Finds orders matching the given filter criteria with optional due date filtering and pagination.
     *
     * @param optionalFilter an optional filter string to match against customer full name
     * @param optionalFilterDate an optional date filter to find orders due after this date
     * @param pageable pagination information
     * @return a page of matching orders
     */
    public Page<Order> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
                                                   Optional<LocalDate> optionalFilterDate, Pageable pageable) {
        if (optionalFilter.isPresent() && !optionalFilter.get().isEmpty()) {
            if (optionalFilterDate.isPresent()) {
                return orderRepository.findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(
                        optionalFilter.get(), optionalFilterDate.get(), pageable);
            } else {
                return orderRepository.findByCustomerFullNameContainingIgnoreCase(optionalFilter.get(), pageable);
            }
        } else {
            if (optionalFilterDate.isPresent()) {
                return orderRepository.findByDueDateAfter(optionalFilterDate.get(), pageable);
            } else {
                return orderRepository.findAll(pageable);
            }
        }
    }

    /**
     * Finds order summaries for orders due starting from today.
     *
     * @return a list of order summaries for orders due today or in the future
     */
    @Transactional
    public List<OrderSummary> findAnyMatchingStartingToday() {
        return orderRepository.findByDueDateGreaterThanEqual(LocalDate.now());
    }

    /**
     * Counts orders matching the given filter criteria with optional due date filtering.
     *
     * @param optionalFilter an optional filter string to match against customer full name
     * @param optionalFilterDate an optional date filter to count orders due after this date
     * @return the count of matching orders
     */
    public long countAnyMatchingAfterDueDate(Optional<String> optionalFilter, Optional<LocalDate> optionalFilterDate) {
        if (optionalFilter.isPresent() && optionalFilterDate.isPresent()) {
            return orderRepository.countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(optionalFilter.get(),
                    optionalFilterDate.get());
        } else if (optionalFilter.isPresent()) {
            return orderRepository.countByCustomerFullNameContainingIgnoreCase(optionalFilter.get());
        } else if (optionalFilterDate.isPresent()) {
            return orderRepository.countByDueDateAfter(optionalFilterDate.get());
        } else {
            return orderRepository.count();
        }
    }

    /**
     * Generates delivery statistics for the current day.
     *
     * @return DeliveryStats object containing various delivery metrics
     */
    private DeliveryStats getDeliveryStats() {
        DeliveryStats stats = new DeliveryStats();
        LocalDate today = LocalDate.now();
        stats.setDueToday((int) orderRepository.countByDueDate(today));
        stats.setDueTomorrow((int) orderRepository.countByDueDate(today.plusDays(1)));
        stats.setDeliveredToday((int) orderRepository.countByDueDateAndStateIn(today,
                Collections.singleton(OrderState.DELIVERED)));

        stats.setNotAvailableToday((int) orderRepository.countByDueDateAndStateIn(today, notAvailableStates));
        stats.setNewOrders((int) orderRepository.countByState(OrderState.NEW));

        return stats;
    }

    /**
     * Generates comprehensive dashboard data for the specified month and year.
     *
     * @param month the month for which to generate data (1-12)
     * @param year the year for which to generate data
     * @return DashboardData object containing various metrics and statistics
     */
    public DashboardData getDashboardData(int month, int year) {
        DashboardData data = new DashboardData();
        data.setDeliveryStats(getDeliveryStats());
        data.setDeliveriesThisMonth(getDeliveriesPerDay(month, year));
        data.setDeliveriesThisYear(getDeliveriesPerMonth(year));

        Number[][] salesPerMonth = new Number[3][12];
        data.setSalesPerMonth(salesPerMonth);
        List<Object[]> sales = orderRepository.sumPerMonthLastThreeYears(OrderState.DELIVERED, year);

        for (Object[] salesData : sales) {
            // year, month, deliveries
            int y = year - (int) salesData[0];
            int m = (int) salesData[1] - 1;
            if (y == 0 && m == month - 1) {
                // skip current month as it contains incomplete data
                continue;
            }
            long count = (long) salesData[2];
            salesPerMonth[y][m] = count;
        }

        LinkedHashMap<Product, Integer> productDeliveries = new LinkedHashMap<>();
        data.setProductDeliveries(productDeliveries);
        for (Object[] result : orderRepository.countPerProduct(OrderState.DELIVERED, year, month)) {
            int sum = ((Long) result[0]).intValue();
            Product p = (Product) result[1];
            productDeliveries.put(p, sum);
        }

        return data;
    }

    /**
     * Gets daily delivery counts for the specified month and year.
     *
     * @param month the month (1-12)
     * @param year the year
     * @return a list of delivery counts for each day of the month
     */
    private List<Number> getDeliveriesPerDay(int month, int year) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        return flattenAndReplaceMissingWithNull(daysInMonth,
                orderRepository.countPerDay(OrderState.DELIVERED, year, month));
    }

    /**
     * Gets monthly delivery counts for the specified year.
     *
     * @param year the year
     * @return a list of delivery counts for each month of the year
     */
    private List<Number> getDeliveriesPerMonth(int year) {
        return flattenAndReplaceMissingWithNull(12, orderRepository.countPerMonth(OrderState.DELIVERED, year));
    }

    /**
     * Converts a list of object arrays into a flattened list with null values for missing indices.
     *
     * @param length the length of the resulting list
     * @param list the list of object arrays containing index-value pairs
     * @return a flattened list with values at their respective indices
     */
    private List<Number> flattenAndReplaceMissingWithNull(int length, List<Object[]> list) {
        List<Number> counts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            counts.add(null);
        }

        for (Object[] result : list) {
            counts.set((Integer) result[0] - 1, (Number) result[1]);
        }
        return counts;
    }

    /**
     * Gets the OrderRepository instance.
     *
     * @return the OrderRepository instance
     */
    @Override
    public JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    /**
     * Creates a new order with default values.
     *
     * @param currentUser the user creating the order
     * @return a new Order instance with default due time and date
     */
    @Override
    @Transactional
    public Order createNew(User currentUser) {
        Order order = new Order(currentUser);
        order.setDueTime(LocalTime.of(16, 0));
        order.setDueDate(LocalDate.now());
        return order;
    }

}