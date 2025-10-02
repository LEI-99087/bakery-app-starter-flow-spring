package com.vaadin.starter.bakery.app.security;

import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Functional interface representing the currently authenticated user.
 * Provides access to the user entity of the currently logged-in user.
 *
 * @see User
 */
@FunctionalInterface
public interface CurrentUser {

    /**
     * Returns the currently authenticated user.
     *
     * @return the User entity representing the currently logged-in user
     */
    User getUser();
}