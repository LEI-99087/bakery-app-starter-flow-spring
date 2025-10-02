package com.vaadin.starter.bakery.app.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 * <p>
 * This utility class provides methods for retrieving current user information
 * and checking authentication status from the Spring Security context.
 * </p>
 */
public final class SecurityUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SecurityUtils() {
        // Util methods only
    }

    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or {@code null} if the user
     *         has not signed in or is anonymous
     */
    public static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            Object principal = context.getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
                return userDetails.getUsername();
            }
        }
        // Anonymous or no authentication.
        return null;
    }

    /**
     * Checks if the user is logged in.
     *
     * @return {@code true} if the user is logged in and not anonymous,
     *         {@code false} otherwise
     */
    public static boolean isUserLoggedIn() {
        return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Checks if the given authentication represents a logged-in user.
     *
     * @param authentication the Authentication object to check
     * @return {@code true} if the authentication is not null and not anonymous,
     *         {@code false} otherwise
     */
    private static boolean isUserLoggedIn(Authentication authentication) {
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

}