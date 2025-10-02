package com.vaadin.starter.bakery.backend.service;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * A data integrity violation exception containing a message intended to be
 * shown to the end user.
 * This exception wraps data integrity violations with user-friendly messages
 * that can be safely displayed in the UI.
 */
public class UserFriendlyDataException extends DataIntegrityViolationException {

    /**
     * Constructs a new UserFriendlyDataException with the specified user-friendly message.
     *
     * @param message the user-friendly message that can be displayed to the end user
     */
    public UserFriendlyDataException(String message) {
        super(message);
    }

}