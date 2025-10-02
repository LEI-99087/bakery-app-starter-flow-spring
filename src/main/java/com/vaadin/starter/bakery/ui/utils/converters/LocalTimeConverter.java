package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;

import java.time.LocalTime;

/**
 * Converter class for formatting LocalTime objects.
 * Formats time values using the application's standard hour formatter.
 */
public class LocalTimeConverter {

    /**
     * Encodes a LocalTime object as a formatted string.
     * Uses the standard hour formatter to format the time value.
     *
     * @param modelValue the LocalTime value to format
     * @return the formatted time string, or null if the input value is null
     */
    public String encode(LocalTime modelValue) {
        return convertIfNotNull(modelValue, HOUR_FORMATTER::format);
    }
}