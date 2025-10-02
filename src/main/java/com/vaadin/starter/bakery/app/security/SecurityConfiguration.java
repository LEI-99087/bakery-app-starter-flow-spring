package com.vaadin.starter.bakery.app.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;
import com.vaadin.starter.bakery.ui.views.login.LoginView;

/**
 * Configures Spring Security for the application, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the current user bean.</li>
 *
 * <p>This configuration is active for all profiles except "control-center".
 *
 * @see VaadinWebSecurity
 * @see LoginView
 * @see CurrentUser
 * @see UserRepository
 */
@EnableWebSecurity
@Configuration
@Profile("!control-center")
public class SecurityConfiguration extends VaadinWebSecurity {

    /**
     * Creates a prototype-scoped bean that provides access to the current authenticated user.
     * The bean is created for each injection point and retrieves the user from the repository
     * based on the current security context username.
     *
     * @param userRepository the repository used to find user by email
     * @return a CurrentUser instance that provides access to the current user entity
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CurrentUser currentUser(UserRepository userRepository) {
        final String username = SecurityUtils.getUsername();
        User user = username != null ? userRepository.findByEmailIgnoreCase(username) : null;
        return () -> user;
    }

    /**
     * Configures HTTP security to require login for accessing internal pages
     * and sets up the custom login view.
     *
     * @param http the HttpSecurity to configure
     * @throws Exception if an error occurs during configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    /**
     * Configures WebSecurity to allow access to static resources without authentication.
     * Bypasses Spring Security for robots.txt, icons, images, and H2 console (in development).
     *
     * @param web the WebSecurity to configure
     * @throws Exception if an error occurs during configuration
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().requestMatchers(
                // the robots exclusion standard
                "/robots.txt",
                // icons and images
                "/icons/**",
                "/images/**",
                // (development mode) H2 debugging console
                "/h2-console/**"
        );
    }
}