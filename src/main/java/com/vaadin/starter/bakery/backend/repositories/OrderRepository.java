package com.vaadin.starter.bakery.backend.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

/**
 * Repository interface for Order entity operations.
 * <p>
 * Provides CRUD operations and custom query methods for Order entities
 * through Spring Data JPA. Includes methods for filtering, pagination,
 * and statistical analysis of order data.
 * </p>
 *
 * @see Order
 * @see JpaRepository
 * @see OrderSummary
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds orders with due date after the specified date, with brief entity graph loading.
     *
     * @param filterDate the date to filter orders after
     * @param pageable pagination information
     * @return page of orders with due date after the filter date
     */
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    Page<Order> findByDueDateAfter(LocalDate filterDate, Pageable pageable);

    /**
     * Finds orders by customer name containing the search query (case-insensitive), with brief entity graph loading.
     *
     * @param searchQuery the string to search for in customer names
     * @param pageable pagination information
     * @return page of orders matching the customer name search
     */
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    Page<Order> findByCustomerFullNameContainingIgnoreCase(String searchQuery, Pageable pageable);

    /**
     * Finds orders by customer name containing the search query (case-insensitive)
     * and with due date after the specified date, with brief entity graph loading.
     *
     * @param searchQuery the string to search for in customer names
     * @param dueDate the date to filter orders after
     * @param pageable pagination information
     * @return page of orders matching both criteria
     */
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    Page<Order> findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate, Pageable pageable);

    /**
     * Finds all orders with brief entity graph loading.
     *
     * @return list of all orders
     */
    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    List<Order> findAll();

    /**
     * Finds all orders with pagination and brief entity graph loading.
     *
     * @param pageable pagination information
     * @return page of all orders
     */
    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    Page<Order> findAll(Pageable pageable);

    /**
     * Finds order summaries with due date greater than or equal to the specified date.
     *
     * @param dueDate the minimum due date to filter by
     * @return list of order summaries matching the due date criteria
     */
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
    List<OrderSummary> findByDueDateGreaterThanEqual(LocalDate dueDate);

    /**
     * Finds an order by ID with full entity graph loading.
     *
     * @param id the order ID to search for
     * @return optional containing the order if found
     */
    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_FULL, type = EntityGraphType.LOAD)
    Optional<Order> findById(Long id);

    /**
     * Counts orders with due date after the specified date.
     *
     * @param dueDate the date to filter orders after
     * @return count of orders with due date after the specified date
     */
    long countByDueDateAfter(LocalDate dueDate);

    /**
     * Counts orders by customer name containing the search query (case-insensitive).
     *
     * @param searchQuery the string to search for in customer names
     * @return count of orders matching the customer name search
     */
    long countByCustomerFullNameContainingIgnoreCase(String searchQuery);

    /**
     * Counts orders by customer name containing the search query (case-insensitive)
     * and with due date after the specified date.
     *
     * @param searchQuery the string to search for in customer names
     * @param dueDate the date to filter orders after
     * @return count of orders matching both criteria
     */
    long countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate);

    /**
     * Counts orders with the specified due date.
     *
     * @param dueDate the exact due date to filter by
     * @return count of orders with the specified due date
     */
    long countByDueDate(LocalDate dueDate);

    /**
     * Counts orders with the specified due date and state in the given collection.
     *
     * @param dueDate the exact due date to filter by
     * @param state collection of order states to filter by
     * @return count of orders matching both criteria
     */
    long countByDueDateAndStateIn(LocalDate dueDate, Collection<OrderState> state);

    /**
     * Counts orders with the specified state.
     *
     * @param state the order state to filter by
     * @return count of orders with the specified state
     */
    long countByState(OrderState state);

    /**
     * Counts orders per month for the specified state and year.
     *
     * @param orderState the order state to filter by
     * @param year the year to filter by
     * @return list of object arrays containing month and delivery count
     */
    @Query("SELECT month(dueDate) as month, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 group by month(dueDate)")
    List<Object[]> countPerMonth(OrderState orderState, int year);

    /**
     * Calculates sales sum per month for the last three years for the specified state.
     *
     * @param orderState the order state to filter by
     * @param year the current year (last three years will be calculated from this)
     * @return list of object arrays containing year, month, and sales sum
     */
    @Query("SELECT year(o.dueDate) as y, month(o.dueDate) as m, sum(oi.quantity*p.price) as deliveries FROM OrderInfo o JOIN o.items oi JOIN oi.product p where o.state=?1 and year(o.dueDate)<=?2 AND year(o.dueDate)>=(?2-3) group by year(o.dueDate), month(o.dueDate) order by y desc, month(o.dueDate)")
    List<Object[]> sumPerMonthLastThreeYears(OrderState orderState, int year);

    /**
     * Counts orders per day for the specified state, year, and month.
     *
     * @param orderState the order state to filter by
     * @param year the year to filter by
     * @param month the month to filter by
     * @return list of object arrays containing day and delivery count
     */
    @Query("SELECT day(dueDate) as day, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 and month(dueDate)=?3 group by day(dueDate)")
    List<Object[]> countPerDay(OrderState orderState, int year, int month);

    /**
     * Counts product quantities sold per product for the specified state, year, and month.
     *
     * @param orderState the order state to filter by
     * @param year the year to filter by
     * @param month the month to filter by
     * @return list of object arrays containing quantity and product
     */
    @Query("SELECT sum(oi.quantity), p FROM OrderInfo o JOIN o.items oi JOIN oi.product p WHERE o.state=?1 AND year(o.dueDate)=?2 AND month(o.dueDate)=?3 GROUP BY p.id ORDER BY p.id")
    List<Object[]> countPerProduct(OrderState orderState, int year, int month);

}