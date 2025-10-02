package com.vaadin.starter.bakery.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HasLogger is a feature interface that provides Logging capability for anyone
 * implementing it where logger needs to operate in serializable environment
 * without being static.
 * <p>
 * This interface provides a default method that returns a logger instance
 * specific to the implementing class, making it suitable for serializable
 * contexts where static loggers are not appropriate.
 * </p>
 */
public interface HasLogger {

    /**
     * Returns a Logger instance for the implementing class.
     *
     * @return a Logger instance configured for the implementing class
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}