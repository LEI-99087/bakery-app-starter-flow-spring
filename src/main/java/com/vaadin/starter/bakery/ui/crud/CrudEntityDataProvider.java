package com.vaadin.starter.bakery.ui.crud;

import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;

/**
 * Data provider for CRUD entities that connects Vaadin components with backend service.
 * Provides paginated, filterable, and sortable data access for entities.
 *
 * @param <T> the type of entity this data provider handles, must extend AbstractEntity
 */
public class CrudEntityDataProvider<T extends AbstractEntity> extends FilterablePageableDataProvider<T, String> {

    private final FilterableCrudService<T> crudService;
    private List<QuerySortOrder> defaultSortOrders;

    /**
     * Constructs a new CrudEntityDataProvider with the specified CRUD service.
     *
     * @param crudService the service used for entity operations and data retrieval
     */
    public CrudEntityDataProvider(FilterableCrudService<T> crudService) {
        this.crudService = crudService;
        setSortOrders();
    }

    /**
     * Sets up the default sort orders for the data provider.
     * By default, sorts by ID in ascending order.
     */
    private void setSortOrders() {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        builder.thenAsc("id");
        defaultSortOrders = builder.build();
    }

    /**
     * Fetches a page of entities from the backend service matching the given query and pagination.
     *
     * @param query the query containing filter and sorting information
     * @param pageable the pagination information
     * @return a page of entities matching the criteria
     */
    @Override
    protected Page<T> fetchFromBackEnd(Query<T, String> query, Pageable pageable) {
        return crudService.findAnyMatching(query.getFilter(), pageable);
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
     * Gets the total count of entities matching the given filter criteria.
     *
     * @param query the query containing filter information
     * @return the total count of matching entities
     */
    @Override
    protected int sizeInBackEnd(Query<T, String> query) {
        return (int) crudService.countAnyMatching(query.getFilter());
    }

}