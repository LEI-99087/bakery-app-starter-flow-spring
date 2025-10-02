/**
 * Configuration class for creating presenter beans used in CRUD operations.
 * Defines bean configurations with appropriate scopes for entity presenters.
 */
package com.vaadin.starter.bakery.ui.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

@Configuration
public class PresenterFactory {

    /**
     * Creates a prototype-scoped EntityPresenter for Order entities.
     * Prototype scope ensures a new instance is created each time the bean is requested.
     *
     * @param crudService the OrderService used for order operations
     * @param currentUser the currently authenticated user
     * @return a new EntityPresenter instance for Order entities
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EntityPresenter<Order, StorefrontView> orderEntityPresenter(OrderService crudService, CurrentUser currentUser) {
        return new EntityPresenter<>(crudService, currentUser);
    }

}