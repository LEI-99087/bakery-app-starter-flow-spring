package com.vaadin.starter.bakery.ui.views.storefront.converters;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;

import java.time.LocalDate;

/**
 * Date converter specific for the way date is displayed in storefront.
 * Converts LocalDate objects to StorefrontDate objects with formatted display values.
 */
public class StorefrontLocalDateConverter {

    /**
     * Converts a LocalDate to a StorefrontDate with formatted display values.
     *
     * @param modelValue the LocalDate to convert, can be null
     * @return a StorefrontDate with formatted day, weekday, and date strings, or null if input is null
     */
    public StorefrontDate encode(LocalDate modelValue) {
        StorefrontDate result = null;
        if (modelValue != null) {
            result = new StorefrontDate();
            result.setDay(MONTH_AND_DAY_FORMATTER.format(modelValue));
            result.setWeekday(WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
            result.setDate(modelValue.toString());
        }
        return result;
    }
}