package com.vaadin.starter.bakery.ui.utils.converters;

import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

/**
 * Utility class for formatting currency values.
 * Provides methods to convert integer values to formatted currency strings.
 */
public class CurrencyFormatter {

    /**
     * Encodes an integer value as a formatted currency string.
     *
     * @param modelValue the integer value to format as currency
     * @return the formatted currency string, or null if the input value is null
     */
    public String encode(Integer modelValue) {
        return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
    }
}