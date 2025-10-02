package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.FULL_DATE_FORMATTER;

import java.time.LocalDateTime;

/**
 * Converter class for formatting LocalDateTime objects.
 * Combines date and time formatting to create a complete timestamp string.
 */
public class LocalDateTimeConverter {
    private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();

    /**
     * Encodes a LocalDateTime object as a formatted string.
     * Combines the full date format with the time format to create a complete timestamp.
     *
     * @param modelValue the LocalDateTime value to format
     * @return the formatted date-time string, or null if the input value is null
     */
    public String encode(LocalDateTime modelValue) {
        return convertIfNotNull(modelValue,
                v -> FULL_DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.encode(v.toLocalTime()));
    }
}