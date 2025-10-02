package com.vaadin.starter.bakery.app.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Provides Beans for the control center security context.
 * This configuration is specifically for the "control-center" profile and
 * supplies security-related beans that might be refactored in the future.
 *
 * <p>Note: These beans might be removed but would require important changes in code.
 *
 * @see CurrentUser
 * @see AuthenticationContext
 * @see User
 */
@Configuration
@Profile("control-center")
public class SecurityConfigurationCC {

    /**
     * Creates a prototype-scoped bean that provides access to the current user
     * in the control center context. Creates a minimal User entity with the
     * principal name from the authentication context as the first name.
     *
     * @param authCtx the AuthenticationContext used to retrieve the principal name
     * @return a CurrentUser instance that provides access to a minimal user entity
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CurrentUser currentUser(AuthenticationContext authCtx) {
        User user = new User();
        user.setFirstName(authCtx.getPrincipalName().orElse(null));
        return () -> user;
    }
}